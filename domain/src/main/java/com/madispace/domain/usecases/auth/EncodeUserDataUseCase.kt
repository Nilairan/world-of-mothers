package com.madispace.domain.usecases.auth

import android.util.Base64

interface EncodeUserDataUseCase {
    operator fun invoke(email: String, password: String): String
}

class EncodeUserDataUseCaseImpl : EncodeUserDataUseCase {
    override fun invoke(email: String, password: String): String {
        val value = "$email:$password".toByteArray()
        return Base64.encodeToString(value, Base64.DEFAULT)
    }
}