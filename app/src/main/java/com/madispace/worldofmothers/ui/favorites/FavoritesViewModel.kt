package com.madispace.worldofmothers.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madispace.domain.models.ProductShort
import com.madispace.domain.usecases.GetFavoritesProductUseCase
import com.madispace.worldofmothers.common.*
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/3/20
 */
class FavoritesViewModel : BaseViewModel() {

    private val getFavoritesProductUseCase: GetFavoritesProductUseCase by inject()

    private val _favoritesListLiveData = MutableLiveData<Event<List<ProductShort>>>()
    val favoritesListLiveData: LiveData<Event<List<ProductShort>>> = _favoritesListLiveData

    override fun onCreate() {
        getFavoritesProductUseCase.invoke()
            .observeUi()
            .doOnSubscribe { _favoritesListLiveData.postValue(Loading()) }
            .subscribeBy(
                onNext = { _favoritesListLiveData.postValue(Success(it)) },
                onError = { _favoritesListLiveData.postValue(Error()) }
            )
            .addTo(compositeDisposable)
    }
}