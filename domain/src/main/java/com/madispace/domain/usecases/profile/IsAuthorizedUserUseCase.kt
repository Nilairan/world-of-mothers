package com.madispace.domain.usecases.profile

import com.madispace.domain.repository.UserRepository

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
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