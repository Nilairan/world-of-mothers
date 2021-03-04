package com.madispace.worldofmothers.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.usecases.GetFavoritesProductUseCase
import com.madispace.worldofmothers.common.BaseViewModel
import com.madispace.worldofmothers.common.Event
import org.koin.core.component.inject

class FavoritesViewModel : BaseViewModel() {

    private val getFavoritesProductUseCase: GetFavoritesProductUseCase by inject()

    private val _favoritesListLiveData = MutableLiveData<Event<List<ProductShort>>>()
    val favoritesListLiveData: LiveData<Event<List<ProductShort>>> = _favoritesListLiveData

    override fun onCreate() {
//        getFavoritesProductUseCase.invoke()
//            .observeUi()
//            .doOnSubscribe { _favoritesListLiveData.postValue(Loading()) }
//            .subscribeBy(
//                onNext = { _favoritesListLiveData.postValue(Success(it)) },
//                onError = { _favoritesListLiveData.postValue(Error()) }
//            )
//            .addTo(compositeDisposable)
    }
}