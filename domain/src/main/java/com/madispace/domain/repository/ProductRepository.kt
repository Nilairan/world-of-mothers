package com.madispace.domain.repository

import com.madispace.domain.models.image.PhotoModel
import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductFilter
import com.madispace.domain.models.product.ProductShort
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProductList(page: Int): Flow<List<ProductShort>>
    fun getProductById(id: Int): Flow<Product>
    fun getFilteredProductList(page: Int, filter: ProductFilter): Flow<List<ProductShort>>

    fun setFavoriteProduct(product: Product): Flow<Boolean>
    fun removeFavoriteProduct(id: Int): Flow<Boolean>
    fun getFavoriteProduct(id: Int): Flow<Product?>
    fun getFavoriteProductList(): Flow<List<Product>>
    fun addNewProduct(
        name: String,
        price: Int,
        info: String,
        material: String,
        size: String,
        status: String,
        address: String,
        categoryId: Int,
        upfile: List<PhotoModel>
    ): Flow<Boolean>

    fun removeProduct(id: Int): Flow<Boolean>
}