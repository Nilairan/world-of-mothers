package com.madispace.worldofmothers.ui.profile.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madispace.domain.usecases.auth.ValidUseCase
import com.madispace.domain.usecases.profile.RegisterUserUseCase
import com.madispace.worldofmothers.common.BaseViewModel
import com.madispace.worldofmothers.common.Event
import com.madispace.worldofmothers.ui.common.UiModel
import org.koin.core.component.inject

class SignUpViewModel : BaseViewModel() {
    private val registerUserUseCase: RegisterUserUseCase by inject()
    private val validUseCase: ValidUseCase by inject()

    private val _validUiModel = MutableLiveData<Event<UiModel>>()
    val validUiModel: LiveData<Event<UiModel>> = _validUiModel

    var name: String = ""
        set(value) {
            field = value
            validField()
        }

    var phone: String = ""
        set(value) {
            field = value
            validField()
        }

    var email: String = ""
        set(value) {
            field = value
            validField()
        }

    var pass: String = ""
        set(value) {
            field = value
            validField()
        }

    var repeatPass = ""
        set(value) {
            field = value
            validField()
        }

    fun auth() {
        registerUserUseCase.invoke()
    }

    private fun validField() {
//        validUseCase.invoke(
//                ValidData.Builder()
//                        .addField(name) { name.length > 2 }
//                        .addField(phone) { phone.length > 10 }
//                        .addField(email, EmailRule())
//                        .addField(pass, PassRule())
//                        .addField(repeatPass) { repeatPass == pass }
//                        .build()
//        ).subscribeBy(
//                onSuccess = {
//                    _validUiModel.postValue(Success(FiledValid))
//                },
//                onError = {
//                    when (it) {
//                        is EmailValidException -> {
//                            _validUiModel.postValue(Success(EmailInvalid))
//                        }
//                        is PassValidException -> {
//                            _validUiModel.postValue(Success(PassInvalid))
//                        }
//                        is CustomFunctionException -> {
////                            TODO fix
//                            _validUiModel.postValue(Success(PassInvalid))
//                        }
//                    }
//                }
//        )
    }
}