package com.madispace.worldofmothers.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madispace.domain.models.ui.CatalogModel
import com.madispace.domain.usecases.GetCatalogModelUseCase
import com.madispace.worldofmothers.common.*
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/1/20
 */
class CatalogViewModel : BaseViewModel() {

    private val getCatalogModelUseCase: GetCatalogModelUseCase by inject()

    private val _uiModelLiveData = MutableLiveData<Event<CatalogModel>>()
    val uiModelLiveData: LiveData<Event<CatalogModel>> = _uiModelLiveData

    override fun onCreate() {
        getCatalogModelUseCase.invoke()
                .observeUi()
                .doOnSubscribe { _uiModelLiveData.postValue(Loading()) }
                .subscribeBy(
                        onNext = { _uiModelLiveData.postValue(Success(it)) },
                        onError = { _uiModelLiveData.postValue(Error()) }
                )
                .addTo(compositeDisposable)
    }

}