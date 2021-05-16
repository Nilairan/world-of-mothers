package com.madispace.domain.usecases.product

import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.ui.ProductModel
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
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
                .zip(productRepository.getFavoriteProduct(productId)) { pair: Pair<Product, List<ProductShort>>, product: Product? ->
                    ProductModel(
                            product = pair.first,
                            seller = pair.first.user,
                            isFavoriteProduct = product != null,
                            additionallyProduct = pair.second
                    )
                }
    }
}
