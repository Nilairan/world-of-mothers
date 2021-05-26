package com.madispace.domain.usecases.profile

import com.madispace.domain.models.ui.ProfileModel
import com.madispace.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

interface GetProfileModelUseCase {
    operator fun invoke(): Flow<ProfileModel>
}

class GetProfileModelUseCaseImpl(
        private val userRepository: UserRepository
) : GetProfileModelUseCase {
    override fun invoke(): Flow<ProfileModel> {
        return userRepository.getUserProfile(true)
            .zip(userRepository.getUserProduct()) { profile, product ->
                ProfileModel(profile, product)
            }
    }
}