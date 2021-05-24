package com.madispace.core.repository

import com.madispace.core.database.entities.ProductEntityMapper
import com.madispace.core.database.entities.ProductMapper
import com.madispace.core.network.datasource.product.ProductDataSource
import com.madispace.domain.models.image.PhotoModel
import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductFilter
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

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

    override fun addNewProduct(
        name: String,
        price: Int,
        info: String,
        material: String,
        size: String,
        status: String,
        address: String,
        categoryId: Int,
        upfile: List<PhotoModel>
    ): Flow<Boolean> {
        return flow {
            val result = productDataSource.addNewProduct(
                name,
                price,
                info,
                material,
                size,
                status,
                address,
                categoryId,
                upfile.map { model ->
                    MultipartBody.Part.createFormData(
                        name = FILE,
                        filename = model.fileName,
                        body = model.file.toRequestBody(model.mediaType.toMediaTypeOrNull())
                    )
                }
            )
            emit(result.status == 200)
        }.flowOn(Dispatchers.IO)
    }

    override fun removeProduct(id: Int): Flow<Boolean> {
        return flow {
            val result = productDataSource.removeProduct(id)
            emit(result.status == 200)
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private const val FILE = "upfile"
    }
}