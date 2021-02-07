package com.madispace.worldofmothers.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.usecases.profile.GetUserProductUseCase
import com.madispace.domain.usecases.profile.IsAuthorizedUserUseCase
import com.madispace.worldofmothers.common.BaseViewModel
import com.madispace.worldofmothers.common.Event
import com.madispace.worldofmothers.common.Success
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
class ProfileViewModel : BaseViewModel() {

    private val isAuthorizedUserUseCase: IsAuthorizedUserUseCase by inject()
    private val getUserProductUseCase: GetUserProductUseCase by inject()

    private val _isAuthorizedUserLiveData = MutableLiveData<Event<Boolean>>()
    val isAuthorizedUserLiveData: LiveData<Event<Boolean>> = _isAuthorizedUserLiveData

    private val _userProductLiveData = MutableLiveData<Event<List<ProductShort>>>()
    val userProductLiveData: LiveData<Event<List<ProductShort>>> = _userProductLiveData

    override fun onCreate() {
        if (isAuthorizedUserUseCase.invoke()) {
            _isAuthorizedUserLiveData.postValue(Success(true))
            getProduct()
        } else {
            _isAuthorizedUserLiveData.postValue(Success(false))
        }
    }

    private fun getProduct() {
//        getUserProductUseCase.invoke()
//                .doOnSubscribe { _userProductLiveData.postValue(Loading()) }
//                .subscribeBy(
//                        onNext = { _userProductLiveData.postValue(Success(it)) },
//                        onError = { _userProductLiveData.postValue(Error()) }
//                )
//                .addTo(compositeDisposable)
    }

}