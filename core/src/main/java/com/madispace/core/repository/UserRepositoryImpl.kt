package com.madispace.core.repository

import com.madispace.domain.repository.UserRepository

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
class UserRepositoryImpl : UserRepository {

    private var authUser: Boolean = false

    override fun isAuthorizedUser(): Boolean {
        return authUser
    }

    override fun auth() {
        authUser = true
    }
}