package com.madispace.worldofmothers.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.databinding.FragmentFavoritesBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.favorites.items.FavoriteProductItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesFragment : ObserveFragment<FavoritesViewModel>(
    FavoritesViewModel::class.java,
    R.layout.fragment_favorites
) {

    private val binding: FragmentFavoritesBinding by viewBinding()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productList.layoutManager = GridLayoutManager(context, 2)
        binding.productList.adapter = adapter
    }

    override fun initObservers() {
        lifecycleScope.launch {
            viewModel.viewStates().collect { state -> state?.let { bindViewState(state) } }
        }
    }

    private fun bindViewState(state: FavoritesViewModel.FavoriteState) {
        with(binding) {
            when (state) {
                is FavoritesViewModel.FavoriteState.ShowLoading -> progressCircular.visibility = View.VISIBLE
                is FavoritesViewModel.FavoriteState.HideLoading -> progressCircular.visibility = View.GONE
                is FavoritesViewModel.FavoriteState.ShowFavoriteProduct -> {
                    adapter.clear()
                    emptyContainer.visibility = View.GONE
                    adapter.addAll(state.products.map {
                        FavoriteProductItem(it, { productId ->
                            router.navigateTo(Screens.ProductScreen(productId))
                        }, { productId ->
                            viewModel.obtainEvent(
                                FavoritesViewModel.FavoriteEvent.RemoveProduct(
                                    productId
                                )
                            )
                        })
                    })
                }
                is FavoritesViewModel.FavoriteState.EmptyFavoriteList -> {
                    adapter.clear()
                    emptyContainer.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}