package com.madispace.core.network.datasource.product

import com.madispace.core.common.TokenManager
import com.madispace.core.database.dao.ProductDao
import com.madispace.core.database.entities.ProductEntity
import com.madispace.core.network.common.Api
import com.madispace.core.network.dto.ApiError
import com.madispace.core.network.dto.product.AddNewProductRequest
import com.madispace.core.network.dto.product.DTOProduct
import com.madispace.core.network.dto.product.DTOProductShort
import com.madispace.domain.exceptions.paging.PageNotFoundException
import com.madispace.domain.models.product.ProductFilter
import kotlinx.coroutines.flow.Flow

interface ProductDataSource {
    suspend fun getAllProductList(page: Int): List<DTOProductShort>
    suspend fun getProductById(id: Int): DTOProduct
    suspend fun setFavoriteProduct(product: ProductEntity)
    suspend fun removeFavoriteProduct(id: Int)
    suspend fun getFavoriteProduct(id: Int): ProductEntity?
    fun getFavoriteProductList(): Flow<List<ProductEntity>>
    suspend fun filteredProductList(page: Int, filter: ProductFilter): List<DTOProductShort>
    suspend fun addNewProduct(request: AddNewProductRequest): ApiError
}

class ProductDataSourceImpl constructor(
    private val api: Api,
    private val productDao: ProductDao,
    private val tokenManager: TokenManager
) : ProductDataSource {

    private var maxCountPage = 1

    override suspend fun getAllProductList(page: Int): List<DTOProductShort> {
        checkMaxPage(page)
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

    override suspend fun filteredProductList(page: Int, filter: ProductFilter): List<DTOProductShort> {
        checkMaxPage(page)
        return when (filter) {
            is ProductFilter.Category -> api.getAllProductList(page = page, categoryId = filter.id)
            is ProductFilter.Min -> api.getAllProductList(page = page, min = filter.value)
            is ProductFilter.Max -> api.getAllProductList(page = page, max = filter.value)
            is ProductFilter.Desc -> api.getAllProductList(page = page, sort = "id")
            is ProductFilter.Asc -> api.getAllProductList(page = page, sort = "-id")
            is ProductFilter.Search -> api.getAllProductList(page = page, value = filter.value)
        }.apply {
            maxCountPage = meta.pageCount
        }.items
    }

    override suspend fun addNewProduct(request: AddNewProductRequest): ApiError {
        return api.addNewProduct(tokenManager.getToken(), request)
    }

    @Throws(PageNotFoundException::class)
    private fun checkMaxPage(page: Int) {
        if (page > maxCountPage) {
            throw PageNotFoundException()
        }
    }
}