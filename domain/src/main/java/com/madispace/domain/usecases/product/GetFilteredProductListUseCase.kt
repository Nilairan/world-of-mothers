package com.madispace.domain.usecases.product

import com.madispace.domain.models.product.ProductFilter
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

interface GetFilteredProductListUseCase {
    operator fun invoke(page: Int, filter: ProductFilter): Flow<List<ProductShort>>
}

class GetFilteredProductListUseCaseImpl(
        private val productRepository: ProductRepository
) : GetFilteredProductListUseCase {
    override fun invoke(page: Int, filter: ProductFilter): Flow<List<ProductShort>> {
        return productRepository.getFilteredProductList(page, filter)
    }
}