package com.madispace.domain.usecases.catalog

import com.madispace.domain.models.product.ProductFilter
import com.madispace.domain.models.ui.CatalogModel
import com.madispace.domain.repository.CategoryRepository
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

sealed class SearchType {
    object Default : SearchType()
    data class Pagination(val page: Int, val filter: ProductFilter = ProductFilter.Default) :
        SearchType()
}

interface GetCatalogModelUseCase {
    operator fun invoke(searchType: SearchType = SearchType.Default): Flow<CatalogModel>
}

class GetCatalogModelUseCaseImpl constructor(
        private val productRepository: ProductRepository,
        private val categoryRepository: CategoryRepository
) : GetCatalogModelUseCase {

    override fun invoke(searchType: SearchType): Flow<CatalogModel> {
        return when (searchType) {
            is SearchType.Default -> {
                productRepository.getAllProductList(1)
                    .zip(categoryRepository.getAllCategory()) { product, category ->
                        CatalogModel(category, product)
                    }
            }
            is SearchType.Pagination ->
                when (searchType.filter) {
                    is ProductFilter.Default -> {
                        productRepository.getAllProductList(searchType.page)
                            .map { CatalogModel(emptyList(), it) }
                    }
                    else -> {
                        productRepository.getFilteredProductList(
                            searchType.page,
                            searchType.filter
                        ).map { CatalogModel(emptyList(), it) }
                    }
                }
        }
    }
}