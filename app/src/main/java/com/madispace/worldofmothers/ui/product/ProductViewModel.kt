package com.madispace.worldofmothers.ui.product

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.User
import com.madispace.domain.usecases.product.GetProductModelUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/7/21
 */
class ProductViewModel :
    BaseMviViewModel<ProductViewModel.ProductState, ProductViewModel.ProductAction, ProductViewModel.ProductEvent>() {

    private val getProductModelUseCase: GetProductModelUseCase by inject()

    override fun obtainEvent(viewEvent: ProductEvent) {
        when (viewEvent) {
            is ProductEvent.LoadProduct -> {
                getProductModel(viewEvent.productId)
            }
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
                    viewState = ProductState.HideLoading
                    viewState = ProductState.ShowProduct(it.product)
                    viewState = ProductState.ShowSeller(it.seller)
                    viewState = ProductState.ShowAdditionallyProduct(it.additionallyProduct)
                }

        }
    }

    sealed class ProductState {
        object ShowLoading : ProductState()
        object HideLoading : ProductState()
        data class ShowProduct(val product: Product) : ProductState()
        data class ShowSeller(val seller: User) : ProductState()
        data class ShowAdditionallyProduct(val additionallyProduct: List<ProductShort>) :
            ProductState()
    }

    sealed class ProductAction {

    }

    sealed class ProductEvent {
        data class LoadProduct(val productId: Int) : ProductEvent()
    }
}