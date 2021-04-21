package com.madispace.core.network.category

import com.madispace.core.network.dto.categories.DTOCategories

interface CategoryDataSource {
    suspend fun getAllCategory(): List<DTOCategories>
}