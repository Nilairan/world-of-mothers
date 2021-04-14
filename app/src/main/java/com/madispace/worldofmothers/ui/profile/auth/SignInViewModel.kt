package com.madispace.worldofmothers.ui.profile.auth

import androidx.lifecycle.viewModelScope
import com.madispace.domain.exceptions.auth.AuthBadFields
import com.madispace.domain.usecases.auth.*
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInViewModel(
    private val validUseCase: ValidUseCase,
    private val authUseCase: AuthUseCase
) : BaseMviViewModel<SignInViewModel.SignInState,
        SignInViewModel.SignInAction, SignInViewModel.SignInEvent>() {

    private var email: String = ""
    private var pass: String = ""

    override fun obtainEvent(viewEvent: SignInEvent) {
        when (viewEvent) {
            is SignInEvent.SetEmail -> email = viewEvent.value
            is SignInEvent.SetPassword -> pass = viewEvent.value
            is SignInEvent.Login -> validFields()
        }
    }

    private fun validFields() {
        viewModelScope.launch(Dispatchers.Main) {
            validUseCase.invoke(
                ValidData.Builder()
                    .addField(email, EmailRule()) { viewAction = SignInAction.EmailNotValid }
                    .addField(pass, { viewAction = SignInAction.PassNotValid }, { pass.isNotEmpty() })
                    .build()
            ).collect {
                if (it) {
                    auth()
                }
            }
        }
    }

    private fun auth() {
        viewModelScope.launch {
            authUseCase.invoke(email, pass)
                .catch {
                    it.printStackTrace()
                    if (it is AuthBadFields) {
                        viewAction = SignInAction.BadFields
                    }
                }
                .collect {
                    viewState = SignInState.SuccessAuth
                }
        }
    }

    sealed class SignInState {
        object SuccessAuth : SignInState()
    }

    sealed class SignInAction {
        object EmailNotValid : SignInAction()
        object PassNotValid : SignInAction()
        object BadFields : SignInAction()
    }

    sealed class SignInEvent {
        data class SetEmail(val value: String) : SignInEvent()
        data class SetPassword(val value: String) : SignInEvent()
        object Login : SignInEvent()
    }
}