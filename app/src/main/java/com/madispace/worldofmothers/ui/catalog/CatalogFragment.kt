package com.madispace.worldofmothers.ui.catalog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.madispace.domain.models.category.Subcategories
import com.madispace.domain.models.product.ProductShort
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.PagingScrollListener
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.databinding.FragmentCatalogBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.catalog.items.CategoryItem
import com.madispace.worldofmothers.ui.catalog.items.ProductItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collect

class CatalogFragment : ObserveFragment<CatalogViewModel>(
    CatalogViewModel::class.java,
    R.layout.fragment_catalog
) {

    private lateinit var backChips: Chip
    private val binding: FragmentCatalogBinding by viewBinding()
    private val categoryListAdapter = GroupAdapter<GroupieViewHolder>()
    private val productListAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            backChips = Chip(getContext()).apply {
                text = getString(R.string.back)
                tag = getString(R.string.back)
                isCheckable = true
                setOnClickListener {
                    binding.chipGroup.isVisible = false
                    binding.categoryList.isVisible = true
                    productListAdapter.clear()
                    viewModel.obtainEvent(CatalogViewModel.CatalogEvent.GetDefaultProducts)
                }
            }
            swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(
                    binding.getContext(),
                    R.color.green
                )
            )
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.obtainEvent(CatalogViewModel.CatalogEvent.Refresh)
            }
            categoryList.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            categoryList.adapter = categoryListAdapter
            val productLayoutManager = GridLayoutManager(context, 2)
            productList.layoutManager = productLayoutManager
            productList.addOnScrollListener(
                PagingScrollListener(layoutManager = productLayoutManager,
                    isLoading = { binding.progressCircular.isVisible },
                    runLoadingBlock = { viewModel.obtainEvent(CatalogViewModel.CatalogEvent.LoadNextProductPage) })
            )
            productList.adapter = productListAdapter
        }
        initFilter()
    }

    private fun initFilter() {
        with(binding) {
            searchItem.filterImage.setOnClickListener { binding.root.openDrawer(Gravity.RIGHT) }
            val items = listOf(
                getString(R.string.new_item),
                getString(R.string.old),
                getString(R.string.asc),
                getString(R.string.desc)
            )
            val adapter = ArrayAdapter(requireContext(), R.layout.item_autocomplite, items)
            filterSheet.filterAutoComplete.setAdapter(adapter)
        }
    }

    override fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewStates().collect { state -> state?.let { bindViewState(state) } }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.viewActions().collect { action -> action?.let { bindViewAction(action) } }
        }
    }

    private fun bindViewState(state: CatalogViewModel.CatalogState) {
        with(binding) {
            when (state) {
                is CatalogViewModel.CatalogState.ShowLoading -> progressCircular.visibility =
                    View.VISIBLE
                is CatalogViewModel.CatalogState.HideLoading -> progressCircular.visibility =
                    View.GONE
                is CatalogViewModel.CatalogState.ShowCategory -> categoryListAdapter.addAll(state.category.map {
                    CategoryItem(it) {
                        viewModel.obtainEvent(CatalogViewModel.CatalogEvent.SelectCategory(it))
                    }
                })
                is CatalogViewModel.CatalogState.ShowProduct -> productListAdapter.addAll(state.products.map {
                    createProductItem(it)
                })
                is CatalogViewModel.CatalogState.ShowRefreshProduct -> {
                    productListAdapter.clear()
                    productListAdapter.addAll(state.products.map { createProductItem(it) })
                }
                is CatalogViewModel.CatalogState.StopRefresh -> swipeRefreshLayout.isRefreshing =
                    false
                is CatalogViewModel.CatalogState.ShowFilteredProduct -> {
                    emptyText.isVisible = state.products.isEmpty()
                    productListAdapter.clear()
                    productListAdapter.addAll(state.products.map {
                        createProductItem(it)
                    })
                }
            }
        }
    }

    private fun createProductItem(product: ProductShort): ProductItem {
        return ProductItem(product, { router.navigateTo(Screens.ProductScreen(product.id)) })
    }

    private fun bindViewAction(action: CatalogViewModel.CatalogAction) {
        when (action) {
            is CatalogViewModel.CatalogAction.ShowErrorMessage -> Toast.makeText(
                binding.getContext(),
                "Error",
                Toast.LENGTH_LONG
            ).show()
            is CatalogViewModel.CatalogAction.ShowSubCategories -> showSubcategory(action.category)
        }
    }

    private fun showSubcategory(category: List<Subcategories>) {
        with(binding) {
            chipGroup.clearCheck()
            chipGroup.removeAllViews()
            categoryList.isVisible = false
            chipGroup.isVisible = true
            chipGroup.addView(backChips)
            category.forEach { sub ->
                val chips = Chip(getContext())
                chips.id = ViewCompat.generateViewId()
                chips.tag = sub
                chips.text = sub.name
                chips.isCheckable = true
                chips.setOnClickListener {
                    viewModel.obtainEvent(
                        CatalogViewModel.CatalogEvent.SelectSubcategory(
                            sub.id,
                            chips.isChecked
                        )
                    )
                }
                chipGroup.addView(chips)
            }
        }
    }

    companion object {
        fun newInstance() = CatalogFragment()
    }
}