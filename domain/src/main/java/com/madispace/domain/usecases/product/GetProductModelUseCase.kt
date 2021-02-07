package com.madispace.domain.usecases.product

import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.ui.ProductModel
import com.madispace.domain.models.user.User
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/7/21
 */
interface GetProductModelUseCase {
    operator fun invoke(productId: Int): Flow<ProductModel>
}

class GetProductModelUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductModelUseCase {
    @FlowPreview
    override fun invoke(productId: Int): Flow<ProductModel> {
        return flow {
            var product: Product? = null
            var seller: User? = null
            var additionallyProduct: List<ProductShort> = listOf()
            productRepository.getProductById(productId)
                .flatMapConcat {
                    product = it
                    getSeller()
                }
                .flatMapConcat {
                    seller = it
                    productRepository.getAllProductList(page = 1)
                }
                .collect { additionallyProduct = it.map { it.mapToShort() } }
            emit(ProductModel(product!!, seller!!, additionallyProduct))
        }.flowOn(Dispatchers.IO)
    }

    //    TODO API not found
    private fun getSeller(): Flow<User> {
        return flow {
            emit(User("Test", 20, ""))
        }
    }

}
