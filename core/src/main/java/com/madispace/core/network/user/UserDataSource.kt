package com.madispace.core.network.user

import com.madispace.core.network.common.Api
import com.madispace.core.network.dto.user.RegisterUserRequest

interface UserDataSource {
    suspend fun registerUser(registerUserRequest: RegisterUserRequest): Any
}

class UserDataSourceImpl(
    private val api: Api
) : UserDataSource {
    override suspend fun registerUser(registerUserRequest: RegisterUserRequest): Any {
        return api.registerUser(registerUserRequest)
    }
}