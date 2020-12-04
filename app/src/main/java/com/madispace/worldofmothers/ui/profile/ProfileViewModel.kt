package com.madispace.worldofmothers.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Screen
import com.madispace.domain.usecases.profile.IsAuthorizedUserUseCase
import com.madispace.worldofmothers.common.BaseViewModel
import com.madispace.worldofmothers.common.Event
import com.madispace.worldofmothers.common.Success
import com.madispace.worldofmothers.routing.Screens
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
class ProfileViewModel : BaseViewModel() {

    private val isAuthorizedUserUseCase: IsAuthorizedUserUseCase by inject()
    private val _routingLiveData = MutableLiveData<Event<Screen>>()
    val routingLiveData: LiveData<Event<Screen>> = _routingLiveData

    override fun onCreate() {
        if (isAuthorizedUserUseCase.invoke()) {

        } else {
            _routingLiveData.postValue(Success(Screens.SignInScreen()))
        }
    }
}