package com.madispace.core.common

import android.util.Base64
import com.madispace.core.database.dao.UserTokenDao
import com.madispace.core.database.entities.TokenEntity

interface TokenManager {
    suspend fun saveTokenByUserId(token: String, id: Int)
    suspend fun getToken(): String
}

class TokenManagerImpl(
    private val userTokenDao: UserTokenDao
) : TokenManager {
    override suspend fun saveTokenByUserId(token: String, id: Int) {
        userTokenDao.clearTable()
        userTokenDao.insertToken(TokenEntity(id, token))
    }

    override suspend fun getToken(): String {
        val token = "${userTokenDao.getToken()?.token ?: ""}:".toByteArray()
        return "$BASIC ${Base64.encodeToString(token, Base64.NO_WRAP)}"
    }

    companion object {
        private const val BASIC = "Basic"
    }

}