package com.madispace.domain.usecases.auth

import com.madispace.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    operator fun invoke(email: String, password: String): Flow<Boolean>
}

class AuthUseCaseImpl(
    private val userRepository: UserRepository,
    private val encodeUserDataUseCase: EncodeUserDataUseCase
) : AuthUseCase {

    override fun invoke(email: String, password: String): Flow<Boolean> {
        return userRepository.auth("$BASIC ${encodeUserDataUseCase.invoke(email, password)}")
    }

    companion object {
        private const val BASIC = "Basic"
    }
}