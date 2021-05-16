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

    private var category: ArrayList<Category> = arrayListOf()

    override fun onCreate() {
        obtainEvent(CreateProductEvent.GetCategories)
    }

    override fun obtainEvent(viewEvent: CreateProductEvent) {
        when (viewEvent) {
            is CreateProductEvent.GetCategories -> getCategories()
        }
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
                    category.apply {
                        clear()
                        addAll(it)
                    }
                    viewState =
                        CreateProductState.ShowCategories(it.map { category -> category.name })
                }
        }
    }

    sealed class CreateProductState {
        data class Loading(val loading: Boolean) : CreateProductState()
        data class ShowCategories(val categories: List<String>) : CreateProductState()
    }

    sealed class CreateProductAction {
        data class ShowError(val error: String) : CreateProductAction()
    }

    sealed class CreateProductEvent {
        object GetCategories : CreateProductEvent()

    }
}