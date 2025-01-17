package com.madispace.core.network.common

import com.madispace.core.common.PaginationResponse
import com.madispace.core.network.dto.ApiError
import com.madispace.core.network.dto.categories.CategoriesResponse
import com.madispace.core.network.dto.product.DTOProduct
import com.madispace.core.network.dto.product.DTOProductShort
import com.madispace.core.network.dto.user.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface Api {

    @GET("${ApiFactory.VERSION}/items")
    suspend fun getAllProductList(
        @Query("page") page: Int,
        @Query("category_id") categoryId: Int? = null,
        @Query("min") min: Int? = null,
        @Query("max") max: Int? = null,
        @Query("search") value: String? = null,
        @Query("sort") sort: String? = null
    ): PaginationResponse<DTOProductShort>

    @GET("${ApiFactory.VERSION}/items/{id}")
    suspend fun getProductById(@Path("id") id: Int): DTOProduct

    @POST("${ApiFactory.VERSION}/reg")
    suspend fun registerUser(@Body registerUserRequest: RegisterUserRequest): DTOResultRegister

    @POST("${ApiFactory.VERSION}/auth")
    suspend fun auth(@Header("Authorization") value: String): DTOAuth

    @GET("${ApiFactory.VERSION}/categories")
    suspend fun getAllCategory(): CategoriesResponse

    @POST("${ApiFactory.VERSION}/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): DTOProfile

    @POST("${ApiFactory.VERSION}/profile/items")
    suspend fun getUserProductList(@Header("Authorization") token: String): PaginationResponse<DTOProductShort>

    @Multipart
    @POST("${ApiFactory.VERSION}/profile/avatar")
    suspend fun uploadAvatar(
        @Header("Authorization") token: String,
        @Part upfile: MultipartBody.Part
    ): ApiError

    @POST("${ApiFactory.VERSION}/profile/edit")
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @Body request: ChangeProfileRequest
    ): ApiError

    @Multipart
    @POST("${ApiFactory.VERSION}/profile/new")
    suspend fun addNewProduct(
        @Header("Authorization") token: String,
        @Part("name") name: String,
        @Part("price") price: Int,
        @Part("info") info: String,
        @Part("material") material: String,
        @Part("size") size: String,
        @Part("status") status: String,
        @Part("address") address: String,
        @Part("category_id") categoryId: Int,
        @Part upfile: List<MultipartBody.Part>
    ): ApiError

    @POST("${ApiFactory.VERSION}/profile/delete")
    @FormUrlEncoded
    suspend fun removeProduct(
        @Header("Authorization") token: String,
        @Field("id") id: Int
    ): ApiError
}