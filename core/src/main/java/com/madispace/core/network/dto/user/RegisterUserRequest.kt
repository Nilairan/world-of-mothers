package com.madispace.core.network.dto.user

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(
    @SerializedName("username") val email: String,
    val password: String,
    @SerializedName("first_name") val firstName: String,
    val surname: String,
    @SerializedName("tel") val phone: String,
)
