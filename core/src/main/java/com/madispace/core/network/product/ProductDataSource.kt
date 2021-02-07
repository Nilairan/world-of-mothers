package com.madispace.core.network.product

import com.madispace.core.dto.DTOProduct
import com.madispace.core.network.Api

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
interface ProductDataSource {
    suspend fun getAllProductList(page: Int): List<DTOProduct>
    suspend fun getProductById(id: Int): DTOProduct
}

class ProductDataSourceImpl constructor(
    private val api: Api
) : ProductDataSource {
    override suspend fun getAllProductList(page: Int): List<DTOProduct> {
        return api.getAllProductList(page = page).items
    }

    override suspend fun getProductById(id: Int): DTOProduct {
        return api.getProductById(id = id)
    }

}