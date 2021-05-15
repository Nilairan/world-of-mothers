package com.madispace.core.network.dto.user

import com.google.gson.annotations.SerializedName

data class DTOAuth(
    val id: Int,
    @SerializedName("access_token") val token: String
)