package com.madispace.core.network.datasource.user

import com.madispace.core.common.TokenManager
import com.madispace.core.database.dao.UserTokenDao
import com.madispace.core.network.common.Api
import com.madispace.core.network.dto.ApiError
import com.madispace.core.network.dto.product.DTOProductShort
import com.madispace.core.network.dto.user.ChangeProfileRequest
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
    suspend fun editProfile(changeProfileRequest: ChangeProfileRequest): DTOProfile
}

class UserDataSourceImpl(
    private val api: Api,
    private val userTokenDao: UserTokenDao,
    private val tokenManager: TokenManager
) : UserDataSource {

    private var profileCache: DTOProfile? = null

    override suspend fun registerUser(registerUserRequest: RegisterUserRequest): String {
        return api.registerUser(registerUserRequest).message
    }

    override suspend fun authUser(value: String): DTOAuth {
        return api.auth(value)
    }

    override suspend fun saveTokenByUserId(token: String, id: Int) {
        tokenManager.saveTokenByUserId(token, id)
    }

    override suspend fun isAuthorizedUser(): Boolean {
        return userTokenDao.getToken() != null
    }

    override suspend fun getProfile(): DTOProfile {
        return profileCache ?: run {
            profileCache = api.getUserProfile(tokenManager.getToken())
            profileCache!!
        }
    }

    override suspend fun getProductList(): List<DTOProductShort> {
        return api.getUserProductList(tokenManager.getToken()).items
    }

    override suspend fun uploadAvatar(file: MultipartBody.Part): ApiError {
        return api.uploadAvatar(tokenManager.getToken(), file)
    }

    override suspend fun editProfile(changeProfileRequest: ChangeProfileRequest): DTOProfile {
        val result = api.editProfile(tokenManager.getToken(), changeProfileRequest)
        if (result.status == 200) {
            profileCache = api.getUserProfile(tokenManager.getToken())
            return profileCache!!
        } else {
            throw RuntimeException("Не удалось сохранить данные")
        }
    }
}