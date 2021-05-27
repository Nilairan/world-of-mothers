package com.madispace.worldofmothers.ui.catalog

import androidx.lifecycle.viewModelScope
import com.madispace.domain.exceptions.paging.PageNotFoundException
import com.madispace.domain.models.category.Category
import com.madispace.domain.models.category.Subcategories
import com.madispace.domain.models.product.ProductFilter
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.usecases.catalog.GetCatalogModelUseCase
import com.madispace.domain.usecases.catalog.SearchType
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val getCatalogModelUseCase: GetCatalogModelUseCase
) : BaseMviViewModel<CatalogViewModel.CatalogState,
        CatalogViewModel.CatalogAction, CatalogViewModel.CatalogEvent>() {

    private var page = 1
        set(value) {
            field = value
            if (value == 1) {
                isAllPageLoaded = false
            }
        }
    private var isAllPageLoaded = false
    private var productFilter: ProductFilter = ProductFilter.Default
    private var currentCategory: Category? = null

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
                if (isAllPageLoaded) return
                page++
                viewState = CatalogState.ShowLoading
                getNextProductPage()
            }
            is CatalogEvent.Refresh -> {
                page = 1
                onRefresh()
            }
            is CatalogEvent.SetFilter -> {
                productFilter = ProductFilter.Filtered(
                    viewEvent.min,
                    viewEvent.max,
                    viewEvent.isNew,
                )
                getProductByFilter()
            }
            is CatalogEvent.SearchFilter -> {
                productFilter = ProductFilter.Search(viewEvent.value)
                getProductByFilter()
            }
            is CatalogEvent.SelectCategory -> {
                page = 1
                currentCategory = viewEvent.category
                getProductByCategory(viewEvent.category)
            }
            is CatalogEvent.SelectSubcategory -> {
                page = 1
                productFilter = if (viewEvent.isChecked) {
                    ProductFilter.Category(viewEvent.id)
                } else {
                    ProductFilter.Category(currentCategory?.id ?: 0)
                }
                getProductByFilter()
            }
            is CatalogEvent.GetDefaultProducts -> {
                page = 1
                productFilter = ProductFilter.Default
                getProductByFilter()
            }
        }
    }

    private fun getCatalogModel() {
        viewModelScope.launch(Dispatchers.Main) {
            getCatalogModelUseCase()
                .onStart { viewState = CatalogState.ShowLoading }
                .catch {
                    it.printStackTrace()
                    viewState = CatalogState.HideLoading
                }
                .collect {
                    viewState = CatalogState.HideLoading
                    viewState = CatalogState.ShowCategory(it.categories)
                    viewState = CatalogState.ShowInitialProduct(it.productsShort)
                }
        }
    }

    private fun getNextProductPage() {
        viewModelScope.launch {
            getCatalogModelUseCase.invoke(
                SearchType.Pagination(
                    page = page,
                    filter = productFilter
                )
            )
                .catch {
                    viewState = CatalogState.HideLoading
                    when (it) {
                        is PageNotFoundException -> isAllPageLoaded = true
                        else -> {
                            page--
                            viewAction = CatalogAction.ShowErrorMessage
                        }
                    }
                }
                .collect {
                    viewState = CatalogState.HideLoading
                    viewState = CatalogState.StopRefresh
                    viewState = CatalogState.ShowProduct(it.productsShort)
                }
        }
    }

    private fun getProductByCategory(category: Category) {
        viewAction = CatalogAction.ShowSubCategories(category = category.subcategories)
        productFilter = ProductFilter.Category(category.id)
        getProductByFilter()
    }

    private fun getProductByFilter() {
        viewModelScope.launch(Dispatchers.Main) {
            viewState = CatalogState.ShowLoading
            getCatalogModelUseCase.invoke(
                SearchType.Pagination(
                    page,
                    productFilter
                )
            ).catch {
                it.printStackTrace()
                viewState = CatalogState.HideLoading
            }.collect {
                viewState = CatalogState.HideLoading
                viewState = CatalogState.ShowFilteredProduct(it.productsShort)
            }
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            getCatalogModelUseCase.invoke(SearchType.Pagination(page, filter = productFilter))
                .catch { viewState = CatalogState.StopRefresh }
                .collect {
                    viewState = CatalogState.StopRefresh
                    viewState = CatalogState.ShowRefreshProduct(it.productsShort)
                }
        }
    }

    sealed class CatalogState {
        object ShowLoading : CatalogState()
        object HideLoading : CatalogState()
        object StopRefresh : CatalogState()
        data class ShowRefreshProduct(val products: List<ProductShort>) : CatalogState()
        data class ShowCategory(val category: List<Category>) : CatalogState()
        data class ShowInitialProduct(val products: List<ProductShort>) : CatalogState()
        data class ShowProduct(val products: List<ProductShort>) : CatalogState()
        data class ShowFilteredProduct(val products: List<ProductShort>) : CatalogState()
    }

    sealed class CatalogAction {
        object ShowErrorMessage : CatalogAction()
        data class ShowSubCategories(val category: List<Subcategories>) : CatalogAction()
    }

    sealed class CatalogEvent {
        object Default : CatalogEvent()
        object LoadNextProductPage : CatalogEvent()
        object Refresh : CatalogEvent()
        data class SetFilter(val min: Int?, val max: Int?, val isNew: Boolean?) : CatalogEvent()
        data class SearchFilter(val value: String) : CatalogEvent()
        data class SelectCategory(val category: Category) : CatalogEvent()
        data class SelectSubcategory(val id: Int, val isChecked: Boolean) : CatalogEvent()
        object GetDefaultProducts : CatalogEvent()
    }
}