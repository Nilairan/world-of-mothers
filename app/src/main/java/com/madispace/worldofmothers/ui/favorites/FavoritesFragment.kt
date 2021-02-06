package com.madispace.worldofmothers.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.madispace.domain.models.ProductShort
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.databinding.FragmentFavoritesBinding
import com.madispace.worldofmothers.ui.favorites.items.FavoriteProductItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/30/20
 */
class FavoritesFragment : ObserveFragment<FavoritesViewModel>(FavoritesViewModel::class) {

    private lateinit var binding: FragmentFavoritesBinding
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initObservers() {
        binding.productList.layoutManager = GridLayoutManager(context, 2)
        binding.productList.adapter = adapter
        viewModel.favoritesListLiveData.observe(
            viewLifecycleOwner,
            object : SimpleObserver<List<ProductShort>>() {
                override fun onSuccess(data: List<ProductShort>) {
                    adapter.addAll(data.map { FavoriteProductItem(it) })
                }

                override fun onLoading(loading: Boolean) {
                    binding.progressCircular.visibility =
                        if (loading) View.VISIBLE else View.INVISIBLE
                }
            })
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}