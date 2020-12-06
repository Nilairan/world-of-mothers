package com.madispace.worldofmothers.ui.profile.auth

import com.madispace.domain.usecases.profile.RegisterUserUseCase
import com.madispace.worldofmothers.common.BaseViewModel
import org.koin.core.component.inject

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/6/20
 */
class SignUpViewModel : BaseViewModel() {
    private val registerUserUseCase: RegisterUserUseCase by inject()

    fun auth() {
        registerUserUseCase.invoke()
    }
}