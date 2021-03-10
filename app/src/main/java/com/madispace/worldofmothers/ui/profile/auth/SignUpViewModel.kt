package com.madispace.worldofmothers.ui.profile.auth

import androidx.lifecycle.viewModelScope
import com.madispace.domain.models.user.RegisterUser
import com.madispace.domain.usecases.auth.*
import com.madispace.domain.usecases.profile.RegisterUserUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SignUpViewModel(
        private val registerUserUseCase: RegisterUserUseCase,
        private val validUseCase: ValidUseCase
) : BaseMviViewModel<SignUpViewModel.SignUpState,
        SignUpViewModel.SignUpAction, SignUpViewModel.SignUpEvent>() {

    private var name = ""
    private var surname = ""
    private var email = ""
    private var phone = ""
    private var password = ""
    private var repeatPassword = ""

    override fun obtainEvent(viewEvent: SignUpEvent) {
        when (viewEvent) {
            is SignUpEvent.SetName -> name = viewEvent.value
            is SignUpEvent.SetSurname -> surname = viewEvent.value
            is SignUpEvent.SetEmail -> email = viewEvent.value
            is SignUpEvent.SetPhone -> phone = viewEvent.value
            is SignUpEvent.SetPassword -> password = viewEvent.value
            is SignUpEvent.SetRepeatPassword -> repeatPassword = viewEvent.value
            is SignUpEvent.ValidateFields -> validFields()
        }
    }


    private fun validFields() {
        viewModelScope.launch(Dispatchers.Main) {
            validUseCase.invoke(
                ValidData.Builder()
                    .addField(
                        name,
                        errorBlock = { viewAction = SignUpAction.NameNotValid },
                        validateBlock = { name.isNotEmpty() },
                    )
                    .addField(surname,
                        errorBlock = { viewAction = SignUpAction.SurnameNotValid },
                        validateBlock = { surname.isNotEmpty() })
                    .addField(
                        email,
                        errorBlock = { viewAction = SignUpAction.EmailNotValid },
                        rule = EmailRule()
                    )
                    .addField(
                        phone,
                        errorBlock = { viewAction = SignUpAction.PhoneNotValid },
                        rule = PhoneRule()
                    )
                    .addField(
                        password,
                        errorBlock = { viewAction = SignUpAction.PasswordNotValid },
                        rule = PassRule()
                    )
                    .addField(repeatPassword,
                        errorBlock = { viewAction = SignUpAction.RepeatPasswordNotValid },
                        validateBlock = { password == repeatPassword })
                    .build()
            ).collect { valid ->
                if (valid) {
                    registerUser()
                } else {
                    viewState = SignUpState.NoValidFields
                }
            }
        }
    }

    private fun registerUser() {
        viewModelScope.launch(Dispatchers.Main) {
            registerUserUseCase.invoke(RegisterUser(name, surname, phone, email, password))
                .onStart { viewState = SignUpState.ShowLoading }
                .catch { viewState = SignUpState.HideLoading }
                .collect {
                    viewState = SignUpState.HideLoading
                    viewAction = SignUpAction.SuccessRegisterUser
                }
        }
    }

    sealed class SignUpState {
        object NoValidFields : SignUpState()
        object ShowLoading : SignUpState()
        object HideLoading : SignUpState()
    }

    sealed class SignUpAction {
        object NameNotValid : SignUpAction()
        object SurnameNotValid : SignUpAction()
        object EmailNotValid : SignUpAction()
        object PhoneNotValid : SignUpAction()
        object PasswordNotValid : SignUpAction()
        object RepeatPasswordNotValid : SignUpAction()
        object SuccessRegisterUser : SignUpAction()
    }

    sealed class SignUpEvent {
        data class SetName(val value: String) : SignUpEvent()
        data class SetSurname(val value: String) : SignUpEvent()
        data class SetEmail(val value: String) : SignUpEvent()
        data class SetPhone(val value: String) : SignUpEvent()
        data class SetPassword(val value: String) : SignUpEvent()
        data class SetRepeatPassword(val value: String) : SignUpEvent()
        object ValidateFields : SignUpEvent()
    }
}