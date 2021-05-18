package com.madispace.worldofmothers.ui.createproduct

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.category.Category
import com.madispace.domain.repository.CategoryRepository
import com.madispace.domain.repository.ProductRepository
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateProductViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : BaseMviViewModel<CreateProductViewModel.CreateProductState,
        CreateProductViewModel.CreateProductAction, CreateProductViewModel.CreateProductEvent>() {

    private var categories: ArrayList<Category> = arrayListOf()
    private var selectCategory: Category? = null

    override fun onCreate() {
        obtainEvent(CreateProductEvent.GetCategories)
    }

    override fun obtainEvent(viewEvent: CreateProductEvent) {
        when (viewEvent) {
            is CreateProductEvent.GetCategories -> getCategories()
            is CreateProductEvent.SelectCategory -> selectCategory(viewEvent.category)
        }
    }

    private fun selectCategory(category: String) {
        selectCategory = categories.find { it.name == category }
    }

    private fun getCategories() {
        viewState = CreateProductState.Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            categoryRepository.getAllCategory()
                .catch {
                    viewState = CreateProductState.Loading(false)
                    it.printStackTrace()
                    viewAction = CreateProductAction.ShowError(it.localizedMessage ?: "")
                }
                .collect {
                    viewState = CreateProductState.Loading(false)
                    categories.apply {
                        clear()
                        addAll(it)
                    }
                    viewAction =
                        CreateProductAction.ShowCategories(it.map { category -> category.name })
                }
        }
    }

    sealed class CreateProductState {
        data class Loading(val loading: Boolean) : CreateProductState()
    }

    sealed class CreateProductAction {
        data class ShowError(val error: String) : CreateProductAction()
        data class ShowCategories(val categories: List<String>) : CreateProductAction()
    }

    sealed class CreateProductEvent {
        object GetCategories : CreateProductEvent()
        data class SelectCategory(val category: String) : CreateProductEvent()

    }
}