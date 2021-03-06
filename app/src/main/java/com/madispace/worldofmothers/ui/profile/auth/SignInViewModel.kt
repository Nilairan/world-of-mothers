package com.madispace.worldofmothers.ui.profile.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madispace.domain.usecases.auth.AuthUseCase
import com.madispace.domain.usecases.auth.ValidUseCase
import com.madispace.worldofmothers.common.BaseViewModel
import com.madispace.worldofmothers.common.Event
import com.madispace.worldofmothers.common.Success
import com.madispace.worldofmothers.ui.common.Default
import com.madispace.worldofmothers.ui.common.UiModel

class SignInViewModel(
    private val validUseCase: ValidUseCase,
    private val authUseCase: AuthUseCase
) : BaseViewModel() {

    private val _validUiModel = MutableLiveData<Event<UiModel>>()
    val validUiModel: LiveData<Event<UiModel>> = _validUiModel

    var email: String = ""
        set(value) {
            field = value
            validFields()
        }
    var pass: String = ""
        set(value) {
            field = value
            validFields()
        }

    override fun onStart() {
        _validUiModel.postValue(Success(Default))
    }

    fun auth() {
        authUseCase.invoke(email, pass)
    }

    private fun validFields() {
//        validUseCase.invoke(
//            ValidData.Builder()
//                .addField(email, EmailRule())
//                .addField(pass, PassRule())
//                .build()
//        )
//            .subscribeBy(
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
//                    }
//                }
//            )
    }
}