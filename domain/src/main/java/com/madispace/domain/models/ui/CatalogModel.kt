package com.madispace.domain.models.ui

import com.madispace.domain.models.Category
import com.madispace.domain.models.ProductShort

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
data class CatalogModel(
        val categories: List<Category>?,
        val productsShort: List<ProductShort>
)
