package com.madispace.worldofmothers.ui.profile.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madispace.domain.exeptions.EmailValidException
import com.madispace.domain.exeptions.PassValidException
import com.madispace.domain.usecases.auth.*
import com.madispace.worldofmothers.common.BaseViewModel
import com.madispace.worldofmothers.common.Event
import com.madispace.worldofmothers.common.Success
import com.madispace.worldofmothers.ui.common.*
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/23/20
 */
class SignInViewModel : BaseViewModel() {

    private val validUseCase: ValidUseCase by inject()
    private val authUseCase: AuthUseCase by inject()

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
        validUseCase.invoke(
            ValidData.Builder()
                .addField(email, EmailRule())
                .addField(pass, PassRule())
                .build()
        )
            .subscribeBy(
                onSuccess = {
                    _validUiModel.postValue(Success(FiledValid))
                },
                onError = {
                    when (it) {
                        is EmailValidException -> {
                            _validUiModel.postValue(Success(EmailInvalid))
                        }
                        is PassValidException -> {
                            _validUiModel.postValue(Success(PassInvalid))
                        }
                    }
                }
            )
    }
}