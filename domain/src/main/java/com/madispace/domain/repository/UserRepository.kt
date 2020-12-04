package com.madispace.domain.repository

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
interface UserRepository {
    fun isAuthorizedUser(): Boolean
    fun auth()
}