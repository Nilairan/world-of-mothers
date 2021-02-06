package com.madispace.domain.usecases

import com.madispace.domain.models.ProductShort
import kotlinx.coroutines.flow.Flow

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/3/20
 */
interface GetFavoritesProductUseCase {
    operator fun invoke(): Flow<List<ProductShort>>
}

class GetFavoritesProductUseCaseImpl : GetFavoritesProductUseCase {
    override fun invoke(): Flow<List<ProductShort>> {
        TODO()
    }

}