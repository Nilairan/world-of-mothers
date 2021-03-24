package com.madispace.core.network.common

import com.madispace.core.common.PaginationResponse
import com.madispace.core.network.dto.product.DTOProduct
import com.madispace.core.network.dto.product.DTOProductShort
import com.madispace.core.network.dto.user.RegisterUserRequest
import retrofit2.http.*

interface Api {

    @GET("${ApiFactory.VERSION}/items")
    suspend fun getAllProductList(
        @Query("page") page: Int
    ): PaginationResponse<DTOProductShort>

    @GET("${ApiFactory.VERSION}/items/{id}")
    suspend fun getProductById(@Path("id") id: Int): DTOProduct

    @POST("reg")
    suspend fun registerUser(@Body registerUserRequest: RegisterUserRequest): Any /*TODO API NOT FOUND*/
}