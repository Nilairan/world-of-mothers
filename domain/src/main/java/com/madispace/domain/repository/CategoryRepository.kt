package com.madispace.domain.repository

import com.madispace.domain.models.category.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategory(): Flow<List<Category>>
}