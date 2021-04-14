package com.madispace.core.repository

import com.madispace.core.network.common.getApiError
import com.madispace.core.network.dto.user.RegisterUserRequest
import com.madispace.core.network.user.UserDataSource
import com.madispace.domain.exceptions.auth.AuthBadFields
import com.madispace.domain.exceptions.register.EmailIsBusy
import com.madispace.domain.exceptions.register.InvalidUserData
import com.madispace.domain.models.user.RegisterUser
import com.madispace.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {

    override fun isAuthorizedUser(): Boolean {
        return userDataSource.isAuthorizedUser()
    }

    override fun auth(value: String): Flow<Boolean> {
        return flow {
            val dtoAuth = userDataSource.authUser(value)
            userDataSource.saveTokenByUserId(dtoAuth.token, dtoAuth.id)
            emit(true)
        }.catch {
            if (it is HttpException && it.code() == 400) {
                throw AuthBadFields()
            } else {
                throw it
            }
        }.flowOn(Dispatchers.IO)

    }

    override fun register(registerUser: RegisterUser): Flow<String> {
        return flow {
            val register = userDataSource.registerUser(
                RegisterUserRequest(
                    email = registerUser.email,
                    firstName = registerUser.name,
                    surname = registerUser.surname,
                    phone = registerUser.phone,
                    password = registerUser.password
                )
            )
            emit(register)
        }.catch {
            if (it is HttpException && it.code() == 400) {
                val error = it.getApiError()
                when (error.code) {
                    EMAIL_IS_BUSY -> throw EmailIsBusy()
                    0 -> throw InvalidUserData()
                    else -> throw it
                }
            } else {
                throw it
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private const val EMAIL_IS_BUSY = 1000
    }
}