package com.madispace.domain.usecases.profile

import com.madispace.domain.repository.UserRepository

interface RegisterUserUseCase {
    operator fun invoke()
}

class RegisterUserUseCaseImpl(
        private val userRepository: UserRepository
) : RegisterUserUseCase {

    override fun invoke() {
        userRepository.auth()
    }

}