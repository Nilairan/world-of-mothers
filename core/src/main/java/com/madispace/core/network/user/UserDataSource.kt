package com.madispace.core.network.user

import com.madispace.core.database.dao.UserTokenDao
import com.madispace.core.database.entities.TokenEntity
import com.madispace.core.network.common.Api
import com.madispace.core.network.dto.user.DTOAuth
import com.madispace.core.network.dto.user.RegisterUserRequest

interface UserDataSource {
    suspend fun registerUser(registerUserRequest: RegisterUserRequest): String
    suspend fun authUser(value: String): DTOAuth
    suspend fun saveTokenByUserId(token: String, id: Int)
    fun isAuthorizedUser(): Boolean
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

    override fun isAuthorizedUser(): Boolean {
        return userTokenDao.getToken() != null
    }
}