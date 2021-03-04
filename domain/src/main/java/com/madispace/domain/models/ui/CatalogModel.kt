package com.madispace.domain.models.ui

import com.madispace.domain.models.category.Category
import com.madispace.domain.models.product.ProductShort

data class CatalogModel(
        val categories: List<Category>?,
        val productsShort: List<ProductShort>
)
