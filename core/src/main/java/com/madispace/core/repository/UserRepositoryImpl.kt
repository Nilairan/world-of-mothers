package com.madispace.core.repository

import com.madispace.core.network.dto.user.RegisterUserRequest
import com.madispace.core.network.user.UserDataSource
import com.madispace.domain.models.user.RegisterUser
import com.madispace.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {

    private var authUser: Boolean = false

    override fun isAuthorizedUser(): Boolean {
        return authUser
    }

    override fun auth() {
        authUser = true
    }

    override fun register(registerUser: RegisterUser): Flow<Boolean> {
        return flow {
            userDataSource.registerUser(
                RegisterUserRequest(
                    email = registerUser.email,
                    firstName = registerUser.name,
                    surname = registerUser.surname,
                    phone = registerUser.phone,
                    password = registerUser.password
                )
            )
            auth()
            emit(true)
        }.flowOn(Dispatchers.IO)
    }
}