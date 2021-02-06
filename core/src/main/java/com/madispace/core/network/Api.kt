package com.madispace.core.network

import com.madispace.core.common.PaginationResponse
import com.madispace.core.dto.Product
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/2/21
 */
interface Api {

    @GET("${ApiFactory.VERSION}/items")
    suspend fun getAllProductList(
        @Query("page") page: Int
    ): PaginationResponse<Product>
}