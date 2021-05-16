package com.madispace.core.network.dto.product

import com.madispace.domain.models.product.ProductShort

data class DTOProductShort(
    val id: Int,
    val name: String,
    val price: Double,
    val img: String
) {
    fun mapToShort(): ProductShort {
        return ProductShort(
            id = id,
            name = name,
            price = price,
            imageUrl = img
        )
    }
}