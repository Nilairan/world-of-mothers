package com.madispace.core.network.dto.user

import com.google.gson.annotations.SerializedName

data class ChangeProfileRequest(
    @SerializedName("first_name") val firstName: String,
    val surname: String,
    val tel: String,
    val size: String = "",
    val status: String = "",
    val address: String = ""
)