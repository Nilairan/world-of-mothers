package com.madispace.core.repository

import com.madispace.core.network.product.ProductDataSource
import com.madispace.domain.models.ProductShort
import com.madispace.domain.repository.ProductListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
class ProductListRepositoryImpl constructor(
    private val productDataSource: ProductDataSource
) : ProductListRepository {
    override fun getAllProductList(page: Int): Flow<List<ProductShort>> {
        return flow {
            emit(productDataSource.getAllProductList(page = page).map { it.mapToShort() })
        }.flowOn(Dispatchers.IO)
    }
}