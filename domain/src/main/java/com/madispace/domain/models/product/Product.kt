package com.madispace.domain.models.product

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/7/21
 */
data class Product(
    val id: Int,
    val userId: Int,
    val categoryId: Int,
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
