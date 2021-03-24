package com.madispace.core.network.dto.product

import com.google.gson.annotations.SerializedName
import com.madispace.domain.models.product.Product

data class DTOProduct(
    val id: Int,
    @SerializedName("category_id") val categoryId: Int,
    val name: String,
    val price: Double,
    val info: String,
    val material: String,
    val size: String,
    val status: String,
    val address: String,
    val gallery: List<String>,
    val user: DTOSeller
) {
    fun mapToModel(): Product {
        return Product(
            id,
            categoryId,
            name,
            price,
            info,
            material,
            size,
            status,
            address,
            gallery,
            user.mapToModel()
        )
    }
}
