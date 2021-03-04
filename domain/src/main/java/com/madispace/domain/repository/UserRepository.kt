package com.madispace.domain.repository

interface UserRepository {
    fun isAuthorizedUser(): Boolean
    fun auth()
}