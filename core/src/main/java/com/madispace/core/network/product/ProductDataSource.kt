package com.madispace.core.network.product

import com.madispace.core.dto.Product
import com.madispace.core.network.Api

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
interface ProductDataSource {
    suspend fun getAllProductList(page: Int): List<Product>
}

class ProductDataSourceImpl constructor(
    private val api: Api
) : ProductDataSource {
    override suspend fun getAllProductList(page: Int): List<Product> {
        return api.getAllProductList(page = page).items
    }

}