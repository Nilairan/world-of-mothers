package com.madispace.domain.models.product

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/26/20
 */
data class ProductShort(
        val id: Int,
        val name: String,
        val imageUrl: String,
        val price: Double
)