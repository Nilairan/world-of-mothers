package com.madispace.worldofmothers.ui.catalog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/30/20
 */
class CatalogFragment : ObserveFragment<CatalogViewModel>(CatalogViewModel::class) {

    private lateinit var binding: FragmentCatalogBinding
    private val categoryListAdapter = GroupAdapter<GroupieViewHolder>()
    private val productListAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchItem.filterImage.setOnClickListener { binding.root.openDrawer(Gravity.RIGHT) }
        val items = listOf("По популярности", "Новые", "По убыванию", "По возрастанию")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_autocomplite, items)
        binding.filterSheet.filterAutoComplete.setText(items[0])
        binding.filterSheet.filterAutoComplete.setAdapter(adapter)

        binding.categoryList.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.categoryList.adapter = categoryListAdapter
        val productLayoutManager = GridLayoutManager(context, 2)
        binding.productList.layoutManager = productLayoutManager
        binding.productList.addOnScrollListener(
            PagingScrollListener(layoutManager = productLayoutManager,
                isLoading = { binding.progressCircular.visibility == View.VISIBLE },
                runLoadingBlock = { viewModel.obtainEvent(CatalogViewModel.CatalogEvent.LoadNextProductPage) })
        )
        binding.productList.adapter = productListAdapter
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
                    CategoryItem(it) { /* TODO click to category */ }
                })
                is CatalogViewModel.CatalogState.ShowProduct -> productListAdapter.addAll(state.products.map {
                    ProductItem(it) { router.navigateTo(Screens.ProductScreen(it.id)) }
                })
            }
        }
    }

    private fun bindViewAction(action: CatalogViewModel.CatalogAction) {
        when (action) {
            is CatalogViewModel.CatalogAction.ShowErrorMessage -> Toast.makeText(
                binding.getContext(),
                "Check",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        fun newInstance() = CatalogFragment()
    }
}