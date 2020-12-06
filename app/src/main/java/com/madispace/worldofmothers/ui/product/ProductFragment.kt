package com.madispace.worldofmothers.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madispace.domain.models.ProductShort
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.databinding.FragmentProductBinding
import com.madispace.worldofmothers.ui.catalog.items.ProductItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/6/20
 */
class ProductFragment : ObserveFragment() {

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = "Товар"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recommendedProductList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val productShortList: ArrayList<ProductShort> = arrayListOf()
        productShortList.apply {
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 240.0))
            add(ProductShort(1, "Соска силиконовая", "", 250.0))
            add(ProductShort(1, "Соска силиконовая", "", 260.0))
            add(ProductShort(1, "Соска силиконовая", "", 270.0))
            add(ProductShort(1, "Соска силиконовая", "", 210.0))
            add(ProductShort(1, "Соска силиконовая", "", 220.0))
            add(ProductShort(1, "Соска силиконовая", "", 290.0))
            add(ProductShort(1, "Соска силиконовая", "", 2320.0))
            add(ProductShort(1, "Соска силиконовая", "", 2430.0))
            add(ProductShort(1, "Соска силиконовая", "", 2230.0))
            add(ProductShort(1, "Соска силиконовая", "", 2340.0))
            add(ProductShort(1, "Соска силиконовая", "", 130.0))
        }
        binding.recommendedProductList.adapter = GroupAdapter<GroupieViewHolder>().apply { addAll(productShortList.map { ProductItem(it) {} }) }
    }

    override fun initObservers() {
    }

    companion object {
        fun newInstance() = ProductFragment()
    }
}