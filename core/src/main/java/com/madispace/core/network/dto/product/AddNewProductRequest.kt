package com.madispace.core.network.dto.product

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class AddNewProductRequest(
    val name: String,
    val price: Int,
    val info: String,
    val material: String,
    val size: String,
    val status: String,
    val address: String,
    @SerializedName("category_id") val categoryId: Int,
    val upfile: List<MultipartBody.Part>
)
