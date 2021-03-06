package com.madispace.worldofmothers.ui.profile

import com.madispace.domain.usecases.profile.GetUserProductUseCase
import com.madispace.domain.usecases.profile.IsAuthorizedUserUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel

class ProfileViewModel(
        private val isAuthorizedUserUseCase: IsAuthorizedUserUseCase,
        private val getUserProductUseCase: GetUserProductUseCase
) : BaseMviViewModel<ProfileViewModel.ProfileState,
        ProfileViewModel.ProfileAction, ProfileViewModel.ProfileEvent>() {

    override fun onCreate() {
        obtainEvent(ProfileEvent.IsAuthUser)
    }

    override fun obtainEvent(viewEvent: ProfileEvent) {
        when (viewEvent) {
            is ProfileEvent.IsAuthUser -> isAuthUser()
        }
    }

    private fun isAuthUser() {
        if (isAuthorizedUserUseCase.invoke()) {
            getProduct()
        } else {
            viewState = ProfileState.OpenSignInScreen
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

    sealed class ProfileState {
        object OpenSignInScreen : ProfileState()
    }


    sealed class ProfileAction {
    }


    sealed class ProfileEvent {
        object IsAuthUser : ProfileEvent()
    }

}