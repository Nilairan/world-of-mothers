package com.madispace.core.network.category

import com.madispace.core.network.common.Api
import com.madispace.core.network.dto.categories.DTOCategories

interface CategoryDataSource {
    suspend fun getAllCategory(): List<DTOCategories>
}

class CategoryDataSourceImpl(
        private val api: Api
) : CategoryDataSource {
    override suspend fun getAllCategory(): List<DTOCategories> {
        return api.getAllCategory().categories
    }
}