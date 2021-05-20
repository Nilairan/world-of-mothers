package com.madispace.worldofmothers.ui.createproduct

import android.net.Uri
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
    private var name: String = ""
    private var status: String = ""
    private var material: String = ""
    private var size: String = ""
    private var price: Int = 0
    private var address: String = ""
    private var description: String = ""
    private val photoList: MutableList<Uri> = mutableListOf()

    override fun onCreate() {
        obtainEvent(CreateProductEvent.GetCategories)
    }

    override fun obtainEvent(viewEvent: CreateProductEvent) {
        when (viewEvent) {
            is CreateProductEvent.GetCategories -> getCategories()
            is CreateProductEvent.SelectCategory -> selectCategory(viewEvent.category)
            is CreateProductEvent.AddPhoto -> addUri(viewEvent.uri)
            is CreateProductEvent.RemovePhoto -> removeUri(viewEvent.uri)
            is CreateProductEvent.SetName -> name = viewEvent.value
            is CreateProductEvent.SetStatus -> status = viewEvent.value
            is CreateProductEvent.SetMaterial -> material = viewEvent.value
            is CreateProductEvent.SetSize -> size = viewEvent.value
            is CreateProductEvent.SetPrice -> price = viewEvent.value
            is CreateProductEvent.SetAddress -> address = viewEvent.value
            is CreateProductEvent.SetDescription -> description = viewEvent.value
        }
    }

    private fun addUri(uri: Uri) {
        photoList.add(uri)
        viewState = CreateProductState.ShowPhotos(photoList)
    }

    private fun removeUri(uri: Uri) {
        photoList.remove(uri)
        viewState = CreateProductState.ShowPhotos(photoList)
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
        data class ShowPhotos(val listUri: List<Uri>) : CreateProductState()
    }

    sealed class CreateProductAction {
        data class ShowError(val error: String) : CreateProductAction()
        data class ShowCategories(val categories: List<String>) : CreateProductAction()
    }

    sealed class CreateProductEvent {
        object GetCategories : CreateProductEvent()
        data class SelectCategory(val category: String) : CreateProductEvent()
        data class AddPhoto(val uri: Uri) : CreateProductEvent()
        data class RemovePhoto(val uri: Uri) : CreateProductEvent()
        data class SetName(val value: String) : CreateProductEvent()
        data class SetStatus(val value: String) : CreateProductEvent()
        data class SetMaterial(val value: String) : CreateProductEvent()
        data class SetSize(val value: String) : CreateProductEvent()
        data class SetPrice(val value: Int) : CreateProductEvent()
        data class SetAddress(val value: String) : CreateProductEvent()
        data class SetDescription(val value: String) : CreateProductEvent()
        object AddProduct : CreateProductEvent()
    }
}