package com.madispace.core.common

import com.google.gson.annotations.SerializedName

data class PaginationResponse<T>(
    val items: List<T>,
    @SerializedName("_meta") val meta: PaginationMetaData
)