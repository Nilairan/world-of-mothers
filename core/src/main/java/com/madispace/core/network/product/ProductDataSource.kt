package com.madispace.core.network.product

import com.madispace.core.database.dao.ProductDao
import com.madispace.core.database.entities.ProductEntity
import com.madispace.core.network.Api
import com.madispace.core.network.dto.DTOProduct
import com.madispace.domain.exceptions.PageNotFoundException
import kotlinx.coroutines.flow.Flow

interface ProductDataSource {
    @Throws(PageNotFoundException::class)
    suspend fun getAllProductList(page: Int): List<DTOProduct>
    suspend fun getProductById(id: Int): DTOProduct
    suspend fun setFavoriteProduct(product: ProductEntity)
    suspend fun removeFavoriteProduct(id: Int)
    suspend fun getFavoriteProduct(id: Int): ProductEntity?
    fun getFavoriteProductList(): Flow<List<ProductEntity>>
}

class ProductDataSourceImpl constructor(
    private val api: Api,
    private val productDao: ProductDao
) : ProductDataSource {

    private var maxCountPage = 1

    override suspend fun getAllProductList(page: Int): List<DTOProduct> {
        if (page > maxCountPage) {
            throw PageNotFoundException()
        }
        val response = api.getAllProductList(page = page)
        maxCountPage = response.meta.pageCount
        return api.getAllProductList(page = page).items
    }

    override suspend fun getProductById(id: Int): DTOProduct {
        return api.getProductById(id = id)
    }

    override suspend fun setFavoriteProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    override suspend fun removeFavoriteProduct(id: Int) {
        productDao.removeById(id)
    }

    override suspend fun getFavoriteProduct(id: Int): ProductEntity? {
        return productDao.getById(id)
    }

    override fun getFavoriteProductList(): Flow<List<ProductEntity>> {
        return productDao.getList()
    }
}