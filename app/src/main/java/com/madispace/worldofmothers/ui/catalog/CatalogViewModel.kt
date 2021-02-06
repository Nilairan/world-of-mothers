package com.madispace.worldofmothers.ui.catalog

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.Category
import com.madispace.domain.models.ProductShort
import com.madispace.domain.usecases.catalog.GetCatalogModelUseCase
import com.madispace.domain.usecases.catalog.SearchModel
import com.madispace.domain.usecases.catalog.SearchType
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/1/20
 */
class CatalogViewModel :
    BaseMviViewModel<CatalogViewModel.CatalogState, CatalogViewModel.CatalogAction, CatalogViewModel.CatalogEvent>() {

    private val getCatalogModelUseCase: GetCatalogModelUseCase by inject()
    private var page = 1

    override fun onCreate() {
        obtainEvent(CatalogEvent.Default)
    }

    override fun obtainEvent(viewEvent: CatalogEvent) {
        when (viewEvent) {
            is CatalogEvent.Default -> {
                page = 1
                getCatalogModel()
            }
            is CatalogEvent.LoadNextProductPage -> {
                page++
                viewState = CatalogState.ShowLoading
                getNextProductPage()
            }
            is CatalogEvent.Refresh -> {

            }
        }
    }

    private fun getCatalogModel() {
        viewModelScope.launch {
            getCatalogModelUseCase(SearchModel())
                .onStart { viewState = CatalogState.ShowLoading }
                .catch {
                    viewState = CatalogState.HideLoading
                    viewAction = CatalogAction.ShowErrorMessage
                }
                .collect {
                    viewState = CatalogState.HideLoading
                    it.categories?.let { category ->
                        viewState = CatalogState.ShowCategory(category)
                    }
                    viewState = CatalogState.ShowProduct(it.productsShort)
                }
        }
    }

    private fun getNextProductPage() {
        viewModelScope.launch {
            getCatalogModelUseCase.invoke(SearchModel(page = page, type = SearchType.PAGINATION))
                .catch {
                    page--
                    viewState = CatalogState.HideLoading
                    viewAction = CatalogAction.ShowErrorMessage
                }
                .collect {
                    viewState = CatalogState.HideLoading
                    viewState = CatalogState.ShowProduct(it.productsShort)
                }
        }
    }

    sealed class CatalogState {
        object ShowLoading : CatalogState()
        object HideLoading : CatalogState()
        data class ShowCategory(val category: List<Category>) : CatalogState()
        data class ShowProduct(val products: List<ProductShort>) : CatalogState()
    }

    sealed class CatalogAction {
        object ShowErrorMessage : CatalogAction()
    }

    sealed class CatalogEvent {
        object Default : CatalogEvent()
        object LoadNextProductPage : CatalogEvent()
        object Refresh : CatalogEvent()
    }
}