package com.madispace.worldofmothers.ui.profile

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.Profile
import com.madispace.domain.usecases.profile.GetProfileModelUseCase
import com.madispace.domain.usecases.profile.IsAuthorizedUserUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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
            is ProfileEvent.Reload -> getProfileModel()
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
        viewModelScope.launch(Dispatchers.Main) {
            viewState = ProfileState.ShowLoading
            getProfileModelUseCase.invoke()
                .catch {
                    viewState = ProfileState.HideLoading
                    it.printStackTrace()
                }
                .collect {
                    viewState = ProfileState.HideLoading
                    viewState = ProfileState.ShowProfileData(it.profile, it.products)
                }
        }
    }

    sealed class ProfileState {
        object OpenSignInScreen : ProfileState()
        object ShowLoading : ProfileState()
        object HideLoading : ProfileState()
        data class ShowProfileData(val profile: Profile, val product: List<ProductShort>) :
            ProfileState()
    }

    sealed class ProfileAction {
    }

    sealed class ProfileEvent {
        object IsAuthUser : ProfileEvent()
        object Reload : ProfileEvent()
    }
}