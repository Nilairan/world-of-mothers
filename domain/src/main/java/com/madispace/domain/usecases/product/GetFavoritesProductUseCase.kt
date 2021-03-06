package com.madispace.domain.usecases.product

import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetFavoritesProductUseCase {
    operator fun invoke(): Flow<List<ProductShort>>
}

class GetFavoritesProductUseCaseImpl constructor(
    private val productRepository: ProductRepository
) : GetFavoritesProductUseCase {
    override fun invoke(): Flow<List<ProductShort>> {
        return productRepository.getFavoriteProductList().map { it.map { it.mapToShort() } }
    }
}