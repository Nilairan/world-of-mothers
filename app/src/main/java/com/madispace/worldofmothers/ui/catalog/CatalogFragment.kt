package com.madispace.worldofmothers.ui.catalog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madispace.domain.models.ui.CatalogModel
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.databinding.FragmentCatalogBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.catalog.items.CategoryItem
import com.madispace.worldofmothers.ui.catalog.items.ProductItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/30/20
 */
class CatalogFragment : ObserveFragment() {

    private lateinit var binding: FragmentCatalogBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val categoryListAdapter = GroupAdapter<GroupieViewHolder>()
    private val productListAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.addLifecycleObserver(lifecycle)
        super.onCreate(savedInstanceState)
    }

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
    }

    override fun initObservers() {
        binding.productList.layoutManager = GridLayoutManager(context, 2)
        binding.productList.adapter = productListAdapter
        binding.categoryList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.categoryList.adapter = categoryListAdapter
        viewModel.uiModelLiveData.observe(viewLifecycleOwner, object : SimpleObserver<CatalogModel>() {
            override fun onSuccess(data: CatalogModel) {
                categoryListAdapter.addAll(data.categories.map { CategoryItem(it) {} })
                productListAdapter.addAll(data.productsShort.map { ProductItem(it) { router.navigateTo(Screens.ProductScreen()) } })
            }

            override fun onLoading(loading: Boolean) {
                binding.progressCircular.visibility = if (loading) View.VISIBLE else View.GONE
            }
        })
    }

    companion object {
        fun newInstance() = CatalogFragment()
    }
}