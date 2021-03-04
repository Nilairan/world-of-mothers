package com.madispace.domain.usecases

import com.madispace.domain.models.product.ProductShort
import kotlinx.coroutines.flow.Flow

interface GetFavoritesProductUseCase {
    operator fun invoke(): Flow<List<ProductShort>>
}

class GetFavoritesProductUseCaseImpl : GetFavoritesProductUseCase {
    override fun invoke(): Flow<List<ProductShort>> {
        TODO()
    }

}