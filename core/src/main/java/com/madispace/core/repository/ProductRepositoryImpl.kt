package com.madispace.core.repository

import com.madispace.core.database.entities.ProductEntityMapper
import com.madispace.core.database.entities.ProductMapper
import com.madispace.core.network.product.ProductDataSource
import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductFilter
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl constructor(
    private val productDataSource: ProductDataSource
) : ProductRepository {

    override fun getAllProductList(page: Int): Flow<List<ProductShort>> {
        return flow {
            emit(productDataSource.getAllProductList(page = page).map { it.mapToShort() })
        }.flowOn(Dispatchers.IO)
    }

    override fun getProductById(id: Int): Flow<Product> {
        return flow {
            emit(productDataSource.getProductById(id = id).mapToModel())
        }.flowOn(Dispatchers.IO)
    }

    override fun getFilteredProductList(page: Int, filter: ProductFilter): Flow<List<ProductShort>> {
        return flow {
            emit(productDataSource.filteredProductList(page, filter).map { it.mapToShort() })
        }.flowOn(Dispatchers.IO)
    }

    override fun setFavoriteProduct(product: Product): Flow<Boolean> {
        return flow {
            try {
                productDataSource.setFavoriteProduct(product = ProductEntityMapper.map(product))
                emit(true)
            } catch (ex: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun removeFavoriteProduct(id: Int): Flow<Boolean> {
        return flow {
            try {
                productDataSource.removeFavoriteProduct(id)
                emit(true)
            } catch (ex: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getFavoriteProduct(id: Int): Flow<Product?> {
        return flow {
            emit(ProductMapper.map(productDataSource.getFavoriteProduct(id)))
        }.flowOn(Dispatchers.IO)
    }

    override fun getFavoriteProductList(): Flow<List<Product>> {
        return productDataSource.getFavoriteProductList().map { it.map { ProductMapper.map(it)!! } }
    }
}