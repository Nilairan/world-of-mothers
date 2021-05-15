package com.madispace.core.network.datasource.user

import android.util.Base64
import com.madispace.core.database.dao.UserTokenDao
import com.madispace.core.database.entities.TokenEntity
import com.madispace.core.network.common.Api
import com.madispace.core.network.dto.ApiError
import com.madispace.core.network.dto.product.DTOProductShort
import com.madispace.core.network.dto.user.DTOAuth
import com.madispace.core.network.dto.user.DTOProfile
import com.madispace.core.network.dto.user.RegisterUserRequest
import okhttp3.MultipartBody

interface UserDataSource {
    suspend fun registerUser(registerUserRequest: RegisterUserRequest): String
    suspend fun authUser(value: String): DTOAuth
    suspend fun saveTokenByUserId(token: String, id: Int)
    suspend fun isAuthorizedUser(): Boolean
    suspend fun getProfile(): DTOProfile
    suspend fun getProductList(): List<DTOProductShort>
    suspend fun uploadAvatar(file: MultipartBody.Part): ApiError
}

class UserDataSourceImpl(
    private val api: Api,
    private val userTokenDao: UserTokenDao
) : UserDataSource {
    override suspend fun registerUser(registerUserRequest: RegisterUserRequest): String {
        return api.registerUser(registerUserRequest).message
    }

    override suspend fun authUser(value: String): DTOAuth {
        return api.auth(value)
    }

    override suspend fun saveTokenByUserId(token: String, id: Int) {
        userTokenDao.clearTable()
        userTokenDao.insertToken(TokenEntity(id, token))
    }

    override suspend fun isAuthorizedUser(): Boolean {
        return userTokenDao.getToken() != null
    }

    override suspend fun getProfile(): DTOProfile {
        return api.getUserProfile(getToken())
    }

    override suspend fun getProductList(): List<DTOProductShort> {
        return api.getUserProductList(getToken()).items
    }

    override suspend fun uploadAvatar(file: MultipartBody.Part): ApiError {
        return api.uploadAvatar(getToken(), file)
    }

    private fun getToken(): String {
        val token = "${userTokenDao.getToken()?.token ?: ""}:".toByteArray()
        return "$BASIC ${Base64.encodeToString(token, Base64.NO_WRAP)}"
    }

    companion object {
        private const val BASIC = "Basic"
    }
}