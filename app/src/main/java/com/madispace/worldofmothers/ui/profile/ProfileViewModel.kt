package com.madispace.worldofmothers.ui.profile

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.Profile
import com.madispace.domain.usecases.profile.GetProfileModelUseCase
import com.madispace.domain.usecases.profile.IsAuthorizedUserUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val isAuthorizedUserUseCase: IsAuthorizedUserUseCase,
    private val getProfileModelUseCase: GetProfileModelUseCase
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
        viewModelScope.launch {
            isAuthorizedUserUseCase.invoke()
                .collect { isAuthorized ->
                    if (isAuthorized) {
                        getProfileModel()
                    } else {
                        viewState = ProfileState.OpenSignInScreen
                    }
                }
        }
    }

    private fun getProfileModel() {
        viewModelScope.launch {
            viewState = ProfileState.ShowLoading
            getProfileModelUseCase.invoke()
                .onEach { viewState = ProfileState.HideLoading }
                .catch {
                    viewState = ProfileState.HideLoading
                    it.printStackTrace()
                }
                .collect {
                    viewAction = ProfileAction.ShowProfile(it.profile)
                    viewAction = ProfileAction.ShowProfileProduct(it.products)
                }
        }
    }

    sealed class ProfileState {
        object OpenSignInScreen : ProfileState()
        object ShowLoading : ProfileState()
        object HideLoading : ProfileState()
    }

    sealed class ProfileAction {
        data class ShowProfile(val profile: Profile) : ProfileAction()
        data class ShowProfileProduct(val product: List<ProductShort>) : ProfileAction()
    }

    sealed class ProfileEvent {
        object IsAuthUser : ProfileEvent()
    }
}