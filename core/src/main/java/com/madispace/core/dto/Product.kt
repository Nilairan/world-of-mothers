package com.madispace.core.dto

import com.google.gson.annotations.SerializedName
import com.madispace.domain.models.ProductShort

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
data class Product(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("category_id") val categoryId: Int,
    val name: String,
    val price: Double,
    val info: String,
    val material: String,
    val size: String,
    val status: String,
    val address: String,
    val img: String
) {
    fun mapToShort(): ProductShort {
        return ProductShort(
            id = id,
            name = name,
            imageUrl = img,
            price = price
        )
    }
}
