package com.madispace.domain.usecases.profile

import com.madispace.domain.repository.UserRepository

interface IsAuthorizedUserUseCase {
    operator fun invoke(): Boolean
}

class IsAuthorizedUserUseCaseImpl(
        private val userRepository: UserRepository
) : IsAuthorizedUserUseCase {

    override fun invoke(): Boolean {
        return userRepository.isAuthorizedUser()
    }
}