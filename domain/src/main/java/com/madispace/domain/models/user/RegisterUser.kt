package com.madispace.domain.models.user

data class RegisterUser(
    val name: String,
    val surname: String,
    val phone: String,
    val email: String,
    val password: String
)