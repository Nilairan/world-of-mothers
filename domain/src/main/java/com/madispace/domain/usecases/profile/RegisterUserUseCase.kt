package com.madispace.domain.usecases.profile

import com.madispace.domain.models.user.RegisterUser
import com.madispace.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

interface RegisterUserUseCase {
    operator fun invoke(registerUser: RegisterUser): Flow<String>
}

class RegisterUserUseCaseImpl(
        private val userRepository: UserRepository
) : RegisterUserUseCase {

    override fun invoke(registerUser: RegisterUser): Flow<String> {
        return userRepository.register(registerUser)
    }
}