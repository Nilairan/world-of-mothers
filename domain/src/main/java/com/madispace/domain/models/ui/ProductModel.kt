package com.madispace.domain.models.ui

import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.User

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/7/21
 */
data class ProductModel(
    val product: Product,
    val seller: User,
    val additionallyProduct: List<ProductShort>
)
