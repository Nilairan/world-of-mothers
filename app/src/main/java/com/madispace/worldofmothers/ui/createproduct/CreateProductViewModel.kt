package com.madispace.worldofmothers.ui.createproduct

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.category.Category
import com.madispace.domain.models.image.PhotoModel
import com.madispace.domain.repository.CategoryRepository
import com.madispace.domain.repository.ProductRepository
import com.madispace.domain.usecases.auth.ValidData
import com.madispace.domain.usecases.auth.ValidUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch

class CreateProductViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val validUseCase: ValidUseCase
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
    private val photoMap: MutableMap<Uri, PhotoModel> = mutableMapOf()

    override fun onCreate() {
        obtainEvent(CreateProductEvent.GetCategories)
    }

    override fun obtainEvent(viewEvent: CreateProductEvent) {
        when (viewEvent) {
            is CreateProductEvent.GetCategories -> getCategories()
            is CreateProductEvent.SelectCategory -> selectCategory(viewEvent.category)
            is CreateProductEvent.AddPhoto -> addUri(viewEvent.uri, viewEvent.model)
            is CreateProductEvent.RemovePhoto -> removeUri(viewEvent.uri)
            is CreateProductEvent.SetName -> name = viewEvent.value
            is CreateProductEvent.SetStatus -> status = viewEvent.value
            is CreateProductEvent.SetMaterial -> material = viewEvent.value
            is CreateProductEvent.SetSize -> size = viewEvent.value
            is CreateProductEvent.SetPrice -> price = viewEvent.value
            is CreateProductEvent.SetAddress -> address = viewEvent.value
            is CreateProductEvent.SetDescription -> description = viewEvent.value
            is CreateProductEvent.AddProduct -> validateData()
        }
    }

    private fun validateData() {
        viewModelScope.launch {
            validUseCase.invoke(
                ValidData.Builder()
                    .addField(
                        name,
                        { viewAction = CreateProductAction.NoValidName },
                        { name.isNotEmpty() })
                    .addField(
                        status,
                        { viewAction = CreateProductAction.NoValidStatus },
                        { status.isNotEmpty() })
                    .addField(
                        material,
                        { viewAction = CreateProductAction.NoValidMaterial },
                        { material.isNotEmpty() })
                    .addField(
                        size,
                        { viewAction = CreateProductAction.NoValidSize },
                        { size.isNotEmpty() })
                    .addField(
                        address,
                        { viewAction = CreateProductAction.NoValidAddress },
                        { address.isNotEmpty() })
                    .addField(
                        description,
                        { viewAction = CreateProductAction.NoValidDescription },
                        { description.isNotEmpty() })
                    .addField(
                        price.toString(),
                        { viewAction = CreateProductAction.NoValidPrice },
                        { price > 0 })
                    .addField(
                        selectCategory.toString(),
                        { viewAction = CreateProductAction.NoValidCategory },
                        { selectCategory != null })
                    .addField(
                        "",
                        { viewAction = CreateProductAction.NoValidPhoto },
                        { photoMap.isNotEmpty() })
                    .build()
            ).flatMapConcat { validate ->
                if (validate) {
                    viewState = CreateProductState.Loading(true)
                    productRepository.addNewProduct(
                        name = name,
                        price = price,
                        info = description,
                        material = material,
                        size = size,
                        status = status,
                        address = address,
                        categoryId = selectCategory?.id ?: 0,
                        upfile = photoMap.toList().map { it.second }
                    )
                } else {
                    throw RuntimeException()
                }
            }.catch {
                it.printStackTrace()
                viewState = CreateProductState.Loading(false)
                viewAction = CreateProductAction.ShowError(it.localizedMessage ?: "")
            }.collect {
                viewState = CreateProductState.Loading(false)
                viewAction = CreateProductAction.SuccessAddProduct
            }
        }
    }

    private fun addUri(uri: Uri, model: PhotoModel) {
        photoMap[uri] = model
        viewState = CreateProductState.ShowPhotos(photoMap.toList().map { it.first })
    }

    private fun removeUri(uri: Uri) {
        photoMap.remove(uri)
        viewState = CreateProductState.ShowPhotos(photoMap.toList().map { it.first })
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
        object SuccessAddProduct : CreateProductAction()
        object NoValidName : CreateProductAction()
        object NoValidStatus : CreateProductAction()
        object NoValidMaterial : CreateProductAction()
        object NoValidSize : CreateProductAction()
        object NoValidAddress : CreateProductAction()
        object NoValidDescription : CreateProductAction()
        object NoValidPrice : CreateProductAction()
        object NoValidCategory : CreateProductAction()
        object NoValidPhoto : CreateProductAction()
    }

    sealed class CreateProductEvent {
        object GetCategories : CreateProductEvent()
        data class SelectCategory(val category: String) : CreateProductEvent()
        data class AddPhoto(val uri: Uri, val model: PhotoModel) : CreateProductEvent()
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