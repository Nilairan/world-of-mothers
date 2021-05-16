package com.madispace.core.network.datasource.category

import com.madispace.core.network.common.Api
import com.madispace.core.network.dto.categories.DTOCategories

interface CategoryDataSource {
    suspend fun getAllCategory(): List<DTOCategories>
}

class CategoryDataSourceImpl(
        private val api: Api
) : CategoryDataSource {

    private var categories: List<DTOCategories>? = null

    override suspend fun getAllCategory(): List<DTOCategories> {
        return categories ?: run {
            categories = api.getAllCategory().categories
            categories!!
        }
    }
}