package com.madispace.domain.repository

import com.madispace.domain.models.product.Product
import kotlinx.coroutines.flow.Flow

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
interface ProductRepository {
    fun getAllProductList(page: Int): Flow<List<Product>>
    fun getProductById(id: Int): Flow<Product>
}