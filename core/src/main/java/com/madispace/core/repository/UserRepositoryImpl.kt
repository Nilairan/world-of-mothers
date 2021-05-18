package com.madispace.core.repository

import com.madispace.core.network.common.getApiError
import com.madispace.core.network.datasource.user.UserDataSource
import com.madispace.core.network.dto.user.ChangeProfileRequest
import com.madispace.core.network.dto.user.RegisterUserRequest
import com.madispace.domain.exceptions.auth.AuthBadFields
import com.madispace.domain.exceptions.register.EmailIsBusy
import com.madispace.domain.exceptions.register.InvalidUserData
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.Profile
import com.madispace.domain.models.user.RegisterUser
import com.madispace.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {

    override fun isAuthorizedUser(): Flow<Boolean> {
        return flow {
            emit(userDataSource.isAuthorizedUser())
        }.flowOn(Dispatchers.IO)
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

    override fun getUserProfile(force: Boolean): Flow<Profile> {
        return flow {
            emit(userDataSource.getProfile(force).mapToModel())
        }.flowOn(Dispatchers.IO)
    }

    override fun getUserProduct(): Flow<List<ProductShort>> {
        return flow {
            emit(userDataSource.getProductList().map { it.mapToShort() })
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun uploadFile(file: ByteArray, mediaType: String, fileName: String): Boolean {
        return try {
            val part = MultipartBody.Part.createFormData(
                name = FILE,
                filename = fileName,
                body = file.toRequestBody(mediaType.toMediaTypeOrNull())
            )
            val response = userDataSource.uploadAvatar(part)
            response.status == 200
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun editProfile(firstName: String, surname: String, tel: String): Flow<Profile> {
        return flow {
            val request = ChangeProfileRequest(firstName = firstName, surname = surname, tel = tel)
            emit(userDataSource.editProfile(request).mapToModel())
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private const val EMAIL_IS_BUSY = 1000

        private const val FILE = "upfile"
    }
}