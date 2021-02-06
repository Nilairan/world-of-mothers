package com.madispace.domain.repository

import com.madispace.domain.models.ProductShort
import kotlinx.coroutines.flow.Flow

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
interface ProductListRepository {
    fun getAllProductList(page: Int): Flow<List<ProductShort>>
}