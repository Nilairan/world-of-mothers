package com.madispace.domain.models.ui

import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.User

data class ProductModel(
    val product: Product,
    val seller: User,
    val isFavoriteProduct: Boolean,
    val additionallyProduct: List<ProductShort>
)
