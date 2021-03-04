package com.madispace.core.repository

import com.madispace.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {

    private var authUser: Boolean = false

    override fun isAuthorizedUser(): Boolean {
        return authUser
    }

    override fun auth() {
        authUser = true
    }
}