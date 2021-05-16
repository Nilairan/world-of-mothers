package com.madispace.domain.usecases.profile

import com.madispace.domain.models.user.RegisterUser
import com.madispace.domain.repository.UserRepository
import com.madispace.domain.usecases.auth.EncodeUserDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

interface RegisterUserUseCase {
    operator fun invoke(registerUser: RegisterUser): Flow<Boolean>
}

class RegisterUserUseCaseImpl(
        private val userRepository: UserRepository,
        private val encodeUserDataUseCase: EncodeUserDataUseCase
) : RegisterUserUseCase {

    override fun invoke(registerUser: RegisterUser): Flow<Boolean> {
        return userRepository.register(registerUser)
                .flatMapLatest {
                    val value = encodeUserDataUseCase.invoke(
                            registerUser.email,
                            registerUser.password
                    )
                    userRepository.auth("$BASIC $value")
                }
    }

    companion object {
        private const val BASIC = "Basic"
    }
}