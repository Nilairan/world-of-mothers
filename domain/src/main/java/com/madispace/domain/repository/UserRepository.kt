package com.madispace.domain.repository

import com.madispace.domain.models.user.RegisterUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun isAuthorizedUser(): Boolean
    fun auth(value: String): Flow<Boolean>
    fun register(registerUser: RegisterUser): Flow<String>
}