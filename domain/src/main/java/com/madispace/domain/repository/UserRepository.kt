package com.madispace.domain.repository

import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.Profile
import com.madispace.domain.models.user.RegisterUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun isAuthorizedUser(): Flow<Boolean>
    fun auth(value: String): Flow<Boolean>
    fun register(registerUser: RegisterUser): Flow<String>
    fun getUserProfile(force: Boolean = false): Flow<Profile>
    fun getUserProduct(): Flow<List<ProductShort>>
    suspend fun uploadFile(file: ByteArray, mediaType: String, fileName: String): Boolean
    fun editProfile(
        firstName: String,
        surname: String,
        tel: String,
    ): Flow<Profile>
}