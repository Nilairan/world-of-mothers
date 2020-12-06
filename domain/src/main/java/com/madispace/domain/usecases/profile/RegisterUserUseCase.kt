package com.madispace.domain.usecases.profile

import com.madispace.domain.repository.UserRepository

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/6/20
 */
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