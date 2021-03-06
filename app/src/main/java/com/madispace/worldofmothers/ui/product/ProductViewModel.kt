package com.madispace.worldofmothers.ui.product

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.User
import com.madispace.domain.usecases.product.FavoriteProductEvent
import com.madispace.domain.usecases.product.FavoriteProductUseCase
import com.madispace.domain.usecases.product.GetProductModelUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductViewModel(
    private val getProductModelUseCase: GetProductModelUseCase,
    private val favoriteProductUseCase: FavoriteProductUseCase
) : BaseMviViewModel<ProductViewModel.ProductState,
        ProductViewModel.ProductAction, ProductViewModel.ProductEvent>() {

    private var isFavoriteProduct: Boolean = false
    private lateinit var product: Product

    override fun obtainEvent(viewEvent: ProductEvent) {
        when (viewEvent) {
            is ProductEvent.LoadProduct -> {
                getProductModel(viewEvent.productId)
            }
            is ProductEvent.OnFavoriteClick -> onFavoriteClick()
        }
    }

    private fun getProductModel(productId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            getProductModelUseCase.invoke(productId = productId)
                .onStart { viewState = ProductState.ShowLoading }
                .catch {
                    viewState = ProductState.HideLoading
                    it.printStackTrace()
                }
                .collect {
                    product = it.product
                    viewState = ProductState.HideLoading
                    viewState = ProductState.ShowProduct(it.product)
                    viewState = ProductState.ShowSeller(it.seller)
                    viewState = ProductState.ShowAdditionallyProduct(it.additionallyProduct)
                    isFavoriteProduct = it.isFavoriteProduct
                    viewState =
                        if (isFavoriteProduct) ProductState.EnableFavorite else ProductState.DisableFavorite
                }
        }
    }

    private fun onFavoriteClick() {
        viewModelScope.launch(Dispatchers.Main) {
            favoriteProductUseCase.invoke(
                event = if (isFavoriteProduct) {
                    FavoriteProductEvent.Remove(id = product.id)
                } else {
                    FavoriteProductEvent.Add(product = product)
                }
            ).collect {
                if (it) {
                    isFavoriteProduct = isFavoriteProduct.not()
                    viewState = if (isFavoriteProduct) {
                        ProductState.EnableFavorite
                    } else {
                        ProductState.DisableFavorite
                    }
                }
            }

        }
    }

    sealed class ProductState {
        object ShowLoading : ProductState()
        object HideLoading : ProductState()
        object EnableFavorite : ProductState()
        object DisableFavorite : ProductState()
        data class ShowProduct(val product: Product) : ProductState()
        data class ShowSeller(val seller: User) : ProductState()
        data class ShowAdditionallyProduct(val additionallyProduct: List<ProductShort>) :
            ProductState()
    }

    sealed class ProductAction

    sealed class ProductEvent {
        data class LoadProduct(val productId: Int) : ProductEvent()
        object OnFavoriteClick : ProductEvent()
    }
}