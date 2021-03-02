package com.madispace.domain.usecases.product

import com.madispace.domain.models.product.Product
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

sealed class FavoriteProductEvent {
    data class Add(val product: Product) : FavoriteProductEvent()
    data class Remove(val id: Int) : FavoriteProductEvent()
}

interface FavoriteProductUseCase {
    operator fun invoke(event: FavoriteProductEvent): Flow<Boolean>
}

class FavoriteProductUseCaseImpl(
    private val productRepository: ProductRepository
) : FavoriteProductUseCase {
    override fun invoke(event: FavoriteProductEvent): Flow<Boolean> {
        return when (event) {
            is FavoriteProductEvent.Add -> productRepository.setFavoriteProduct(event.product)
            is FavoriteProductEvent.Remove -> productRepository.removeFavoriteProduct(event.id)
        }
    }
}