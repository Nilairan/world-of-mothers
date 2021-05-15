package com.madispace.domain.usecases.profile

import com.madispace.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

interface IsAuthorizedUserUseCase {
    operator fun invoke(): Flow<Boolean>
}

class IsAuthorizedUserUseCaseImpl(
        private val userRepository: UserRepository
) : IsAuthorizedUserUseCase {

    override fun invoke(): Flow<Boolean> {
        return userRepository.isAuthorizedUser()
    }
}