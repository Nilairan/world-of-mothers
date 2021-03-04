package com.madispace.worldofmothers.ui.favorites

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.usecases.GetFavoritesProductUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class FavoritesViewModel :
    BaseMviViewModel<FavoritesViewModel.FavoriteState, FavoritesViewModel.FavoriteAction, FavoritesViewModel.FavoriteEvent>() {

    private val getFavoritesProductUseCase: GetFavoritesProductUseCase by inject()

    override fun onCreate() {
        obtainEvent(FavoriteEvent.LoadProduct)
    }

    override fun obtainEvent(viewEvent: FavoriteEvent) {
        when (viewEvent) {
            is FavoriteEvent.LoadProduct -> loadFavoriteProduct()
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

    sealed class FavoriteState {
        object ShowLoading : FavoriteState()
        object HideLoading : FavoriteState()
        data class ShowFavoriteProduct(val products: List<ProductShort>) : FavoriteState()
        object EmptyFavoriteList : FavoriteState()
    }

    sealed class FavoriteAction

    sealed class FavoriteEvent {
        object LoadProduct : FavoriteEvent()
    }
}