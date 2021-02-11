package com.madispace.domain.usecases.product

import com.madispace.domain.models.ui.ProductModel
import com.madispace.domain.models.user.User
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

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
        return productRepository.getProductById(productId)
            .zip(productRepository.getAllProductList(page = 1)) { product, list -> product to list }
            .zip(getSeller()) { (product, list), seller ->
                ProductModel(product, seller, list.map { it.mapToShort() })
            }
    }

    //    TODO API not found
    private fun getSeller(): Flow<User> {
        return flow {
            emit(User("Test", 20, ""))
        }
    }

}
