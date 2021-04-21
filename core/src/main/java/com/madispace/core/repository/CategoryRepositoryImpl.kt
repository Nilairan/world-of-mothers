package com.madispace.core.repository

import com.madispace.core.network.category.CategoryDataSource
import com.madispace.domain.models.category.Category
import com.madispace.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CategoryRepositoryImpl(
        private val categoryDataSource: CategoryDataSource
) : CategoryRepository {

    override fun getAllCategory(): Flow<List<Category>> {
        return flow {
            val categories = categoryDataSource.getAllCategory()
            emit(categories.map { it.mapToModel() })
        }.flowOn(Dispatchers.IO)
    }
}