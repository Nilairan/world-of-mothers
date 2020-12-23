package com.madispace.domain.usecases.auth

import com.madispace.domain.repository.UserRepository

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/23/20
 */
interface AuthUseCase {
    operator fun invoke(email: String, password: String)
}

class AuthUseCaseImpl(
    private val userRepository: UserRepository
) : AuthUseCase {

    override fun invoke(email: String, password: String) {
        userRepository.auth()
    }
}