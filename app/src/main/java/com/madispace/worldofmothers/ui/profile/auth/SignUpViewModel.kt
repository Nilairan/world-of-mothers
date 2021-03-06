package com.madispace.worldofmothers.ui.profile.auth

import androidx.lifecycle.viewModelScope
import com.madispace.domain.usecases.auth.EmailRule
import com.madispace.domain.usecases.auth.PassRule
import com.madispace.domain.usecases.auth.ValidData
import com.madispace.domain.usecases.auth.ValidUseCase
import com.madispace.domain.usecases.profile.RegisterUserUseCase
import com.madispace.worldofmothers.common.BaseMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpViewModel(
        private val registerUserUseCase: RegisterUserUseCase,
        private val validUseCase: ValidUseCase
) : BaseMviViewModel<SignUpViewModel.SignUpState,
        SignUpViewModel.SignUpAction, SignUpViewModel.SignUpEvent>() {

    private var name = ""
    private var email = ""
    private var phone = ""
    private var password = ""
    private var repeatPassword = ""

    override fun obtainEvent(viewEvent: SignUpEvent) {
        when (viewEvent) {
            is SignUpEvent.SetName -> name = viewEvent.value
            is SignUpEvent.SetEmail -> email = viewEvent.value
            is SignUpEvent.SetPhone -> phone = viewEvent.value
            is SignUpEvent.SetPassword -> password = viewEvent.value
            is SignUpEvent.SetRepeatPassword -> repeatPassword = viewEvent.value
            is SignUpEvent.ValidateFields -> validFields()
        }
    }

    sealed class SignUpState {
        object NoValidFields : SignUpState()
    }

    sealed class SignUpAction {
        object NameNotValid : SignUpAction()
        object EmailNotValid : SignUpAction()
        object PhoneNotValid : SignUpAction()
        object PasswordNotValid : SignUpAction()
        object RepeatPasswordNotValid : SignUpAction()
    }

    private fun validFields() {
        viewModelScope.launch(Dispatchers.Main) {
            validUseCase.invoke(ValidData.Builder()
                    .addField(name,
                            errorBlock = { viewAction = SignUpAction.NameNotValid },
                            validateBlock = { name.isNotEmpty() })
                    .addField(email,
                            errorBlock = { viewAction = SignUpAction.EmailNotValid },
                            rule = EmailRule())
                    .addField(phone,
                            errorBlock = { viewAction = SignUpAction.PhoneNotValid },
                            rule = PassRule())
                    .addField(password,
                            errorBlock = { viewAction = SignUpAction.PasswordNotValid },
                            rule = PassRule())
                    .addField(repeatPassword,
                            errorBlock = { viewAction = SignUpAction.RepeatPasswordNotValid },
                            validateBlock = { password == repeatPassword })
                    .build()
            ).collect { valid ->
                if (valid) {
                    /*TODO REGISTER*/
                } else {
                    viewState = SignUpState.NoValidFields
                }
            }
        }
    }

    sealed class SignUpEvent {
        data class SetName(val value: String) : SignUpEvent()
        data class SetEmail(val value: String) : SignUpEvent()
        data class SetPhone(val value: String) : SignUpEvent()
        data class SetPassword(val value: String) : SignUpEvent()
        data class SetRepeatPassword(val value: String) : SignUpEvent()
        object ValidateFields : SignUpEvent()
    }
}