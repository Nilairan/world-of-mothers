package com.madispace.worldofmothers.ui.profile.change

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.user.Profile
import com.madispace.domain.repository.UserRepository
import com.madispace.domain.usecases.auth.PhoneRule
import com.madispace.domain.usecases.auth.ValidData
import com.madispace.domain.usecases.auth.ValidUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ChangeProfileViewModel(
    private val userRepository: UserRepository,
    private val validUseCase: ValidUseCase
) : BaseMviViewModel<ChangeProfileViewModel.ChangeProfileState,
        ChangeProfileViewModel.ChangeProfileAction, ChangeProfileViewModel.ChangeProfileEvent>() {

    private var name: String = ""
    private var surname: String = ""
    private var phone: String = ""

    override fun onCreate() {
        obtainEvent(ChangeProfileEvent.GetProfile)
    }

    override fun obtainEvent(viewEvent: ChangeProfileEvent) {
        when (viewEvent) {
            is ChangeProfileEvent.GetProfile -> getProfile()
            is ChangeProfileEvent.SetName -> name = viewEvent.value
            is ChangeProfileEvent.SetSurname -> surname = viewEvent.value
            is ChangeProfileEvent.SetPhone -> phone = viewEvent.value
            is ChangeProfileEvent.ChangeProfile -> changeProfile()
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            viewState = ChangeProfileState.Loading(loading = true)
            userRepository.getUserProfile()
                .onEach { viewState = ChangeProfileState.Loading(loading = false) }
                .catch {
                    viewState = ChangeProfileState.Loading(loading = false)
                    it.printStackTrace()
                    viewAction = ChangeProfileAction.ShowError(it.localizedMessage ?: "")
                }
                .collect {
                    viewState = ChangeProfileState.ShowProfile(it)
                }
        }
    }

    private fun changeProfile() {
        viewModelScope.launch {
            validUseCase.invoke(
                ValidData.Builder()
                    .addField(
                        name,
                        errorBlock = { viewAction = ChangeProfileAction.NameNotValid },
                        validateBlock = { name.isNotEmpty() },
                    )
                    .addField(surname,
                        errorBlock = { viewAction = ChangeProfileAction.SurnameNotValid },
                        validateBlock = { surname.isNotEmpty() })
                    .addField(
                        phone,
                        errorBlock = { viewAction = ChangeProfileAction.PhoneNotValid },
                        rule = PhoneRule()
                    )
                    .build()
            ).collect { valid ->
                if (valid) {
                    editProfile()
                }
            }
        }
    }

    private fun editProfile() {
        viewModelScope.launch {
            viewState = ChangeProfileState.Loading(loading = true)
            userRepository.editProfile(name, surname, phone)
                .onEach { viewState = ChangeProfileState.Loading(loading = false) }
                .catch {
                    it.printStackTrace()
                    viewState = ChangeProfileState.Loading(loading = false)
                    viewAction = ChangeProfileAction.ShowError(it.localizedMessage ?: "")
                }
                .collect {
                    getProfile()
                }
        }
    }

    sealed class ChangeProfileState {
        data class Loading(val loading: Boolean) : ChangeProfileState()
        data class ShowProfile(val profile: Profile) : ChangeProfileState()
    }

    sealed class ChangeProfileAction {
        data class ShowError(val error: String) : ChangeProfileAction()
        object NameNotValid : ChangeProfileAction()
        object SurnameNotValid : ChangeProfileAction()
        object PhoneNotValid : ChangeProfileAction()
    }

    sealed class ChangeProfileEvent {
        object GetProfile : ChangeProfileEvent()
        data class SetName(val value: String) : ChangeProfileEvent()
        data class SetSurname(val value: String) : ChangeProfileEvent()
        data class SetPhone(val value: String) : ChangeProfileEvent()
        object ChangeProfile : ChangeProfileEvent()
    }
}