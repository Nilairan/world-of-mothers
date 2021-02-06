package com.madispace.core.common

import com.google.gson.annotations.SerializedName

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/6/21
 */
data class PaginationResponse<T>(
    val items: List<T>,
    @SerializedName("_meta") val meta: PaginationMetaData
)