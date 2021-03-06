package com.madispace.worldofmothers.ui.favorites

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.usecases.product.FavoriteProductEvent
import com.madispace.domain.usecases.product.FavoriteProductUseCase
import com.madispace.domain.usecases.product.GetFavoritesProductUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoritesProductUseCase: GetFavoritesProductUseCase,
    private val favoriteProductUseCase: FavoriteProductUseCase
) : BaseMviViewModel<FavoritesViewModel.FavoriteState,
        FavoritesViewModel.FavoriteAction, FavoritesViewModel.FavoriteEvent>() {

    override fun onCreate() {
        obtainEvent(FavoriteEvent.LoadProduct)
    }

    override fun obtainEvent(viewEvent: FavoriteEvent) {
        when (viewEvent) {
            is FavoriteEvent.LoadProduct -> loadFavoriteProduct()
            is FavoriteEvent.RemoveProduct -> removeProduct(productId = viewEvent.id)
        }
    }

    private fun loadFavoriteProduct() {
        viewModelScope.launch(Dispatchers.Main) {
            getFavoritesProductUseCase.invoke()
                .onStart { viewState = FavoriteState.ShowLoading }
                .catch {
                    viewState = FavoriteState.HideLoading
                    it.printStackTrace()
                }
                .collect {
                    viewState = FavoriteState.HideLoading
                    viewState = if (it.isNotEmpty()) {
                        FavoriteState.ShowFavoriteProduct(it)
                    } else {
                        FavoriteState.EmptyFavoriteList
                    }
                }
        }
    }

    private fun removeProduct(productId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            favoriteProductUseCase.invoke(FavoriteProductEvent.Remove(productId))
                .collect()
        }
    }

    sealed class FavoriteState {
        object ShowLoading : FavoriteState()
        object HideLoading : FavoriteState()
        data class ShowFavoriteProduct(val products: List<ProductShort>) : FavoriteState()
        object EmptyFavoriteList : FavoriteState()
    }

    sealed class FavoriteAction

    sealed class FavoriteEvent {
        object LoadProduct : FavoriteEvent()
        data class RemoveProduct(val id: Int) : FavoriteEvent()
    }
}