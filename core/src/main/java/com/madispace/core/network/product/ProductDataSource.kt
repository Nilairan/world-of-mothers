package com.madispace.core.network.product

import com.madispace.core.dto.DTOProduct
import com.madispace.core.network.Api
import com.madispace.domain.exceptions.PageNotFoundException

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
interface ProductDataSource {
    @Throws(PageNotFoundException::class)
    suspend fun getAllProductList(page: Int): List<DTOProduct>
    suspend fun getProductById(id: Int): DTOProduct
}

class ProductDataSourceImpl constructor(
    private val api: Api
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

}