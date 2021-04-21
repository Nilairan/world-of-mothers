package com.madispace.worldofmothers.ui.profile

import com.madispace.domain.usecases.profile.IsAuthorizedUserUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel

class ProfileViewModel(
        private val isAuthorizedUserUseCase: IsAuthorizedUserUseCase,
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
            getProfileModel()
        } else {
            viewState = ProfileState.OpenSignInScreen
        }
    }

    private fun getProfileModel() {

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