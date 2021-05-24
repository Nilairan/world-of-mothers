package com.madispace.domain.usecases.catalog

import com.madispace.domain.models.product.ProductFilter
import com.madispace.domain.models.ui.CatalogModel
import com.madispace.domain.repository.CategoryRepository
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

enum class SearchType {
    DEFAULT,
    PAGINATION,
    REFRESH,
    CATEGORIES
}

data class SearchModel(
    var page: Int = 1,
    var searchValue: String = "",
    var category: Int = 0,
    var type: SearchType = SearchType.DEFAULT
)

interface GetCatalogModelUseCase {
    operator fun invoke(searchModel: SearchModel): Flow<CatalogModel>
}

class GetCatalogModelUseCaseImpl constructor(
        private val productRepository: ProductRepository,
        private val categoryRepository: CategoryRepository
) : GetCatalogModelUseCase {

    override fun invoke(searchModel: SearchModel): Flow<CatalogModel> {
        return when (searchModel.type) {
            SearchType.DEFAULT -> {
                productRepository.getAllProductList(searchModel.page)
                    .zip(categoryRepository.getAllCategory()) { product, category ->
                        CatalogModel(category, product)
                    }
            }
            SearchType.PAGINATION, SearchType.REFRESH -> {
                productRepository.getAllProductList(searchModel.page)
                    .map { CatalogModel(emptyList(), it) }
            }
            SearchType.CATEGORIES -> {
                productRepository.getFilteredProductList(
                    searchModel.page,
                    ProductFilter.Category(searchModel.category)
                )
                    .map { CatalogModel(emptyList(), it) }
            }
        }

    }
}