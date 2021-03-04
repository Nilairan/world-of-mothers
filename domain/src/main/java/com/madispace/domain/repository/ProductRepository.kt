package com.madispace.domain.repository

import com.madispace.domain.models.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProductList(page: Int): Flow<List<Product>>
    fun getProductById(id: Int): Flow<Product>

    fun setFavoriteProduct(product: Product): Flow<Boolean>
    fun removeFavoriteProduct(id: Int): Flow<Boolean>
    fun getFavoriteProduct(id: Int): Flow<Product?>
    fun getFavoriteProductList(): Flow<List<Product>>
}