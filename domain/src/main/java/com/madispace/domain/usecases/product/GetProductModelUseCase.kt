package com.madispace.domain.usecases.product

import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.ui.ProductModel
import com.madispace.domain.models.user.User
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

interface GetProductModelUseCase {
    operator fun invoke(productId: Int): Flow<ProductModel>
}

class GetProductModelUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductModelUseCase {

    override fun invoke(productId: Int): Flow<ProductModel> {
        return productRepository.getProductById(productId)
            .zip(productRepository.getAllProductList(page = 1)) { product, list -> product to list }
            .zip(getSeller()) { pair: Pair<Product, List<ProductShort>>, seller: User ->
                pair to seller
            }
            .zip(productRepository.getFavoriteProduct(productId)) { pair: Pair<Pair<Product, List<ProductShort>>, User>, product: Product? ->
                ProductModel(
                    product = pair.first.first,
                    seller = pair.second,
                    isFavoriteProduct = product != null,
                    additionallyProduct = pair.first.second
                )
            }
    }

    //    TODO API not found
    private fun getSeller(): Flow<User> {
        return flow {
            emit(User("Test", 20, ""))
        }
    }

}
