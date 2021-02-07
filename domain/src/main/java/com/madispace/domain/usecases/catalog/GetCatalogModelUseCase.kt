package com.madispace.domain.usecases.catalog

import com.madispace.domain.models.category.Category
import com.madispace.domain.models.ui.CatalogModel
import com.madispace.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
enum class SearchType {
    DEFAULT,
    PAGINATION
}

data class SearchModel(
    var page: Int = 1,
    var searchValue: String = "",
    var type: SearchType = SearchType.DEFAULT
)

interface GetCatalogModelUseCase {
    operator fun invoke(searchModel: SearchModel): Flow<CatalogModel>
}

class GetCatalogModelUseCaseImpl constructor(
    private val productRepository: ProductRepository
) : GetCatalogModelUseCase {

    override fun invoke(searchModel: SearchModel): Flow<CatalogModel> {
        return when (searchModel.type) {
            SearchType.DEFAULT -> {
                val categoryFlow = flow {
                    val categoryList: ArrayList<Category> = arrayListOf()
                    categoryList.apply {
                        add(Category("Для малышей", ""))
                        add(Category("Дети до 3-х лет", ""))
                        add(Category("Дети до 7-ми лет", ""))
                        add(Category("Дети до 10-ми лет", ""))
                        add(Category("Дети до 11-ми лет", ""))
                        add(Category("Дети до 12-ми лет", ""))
                        add(Category("Дети до 13-ми лет", ""))
                        add(Category("Дети до 15-ми лет", ""))
                        add(Category("Дети до 18-ми лет", ""))
                    }
                    emit(categoryList)
                }
                productRepository.getAllProductList(searchModel.page)
                    .zip(categoryFlow) { product, category ->
                        CatalogModel(category, product.map { it.mapToShort() })
                    }
            }
            SearchType.PAGINATION -> {
                productRepository.getAllProductList(searchModel.page)
                    .map { CatalogModel(emptyList(), it.map { it.mapToShort() }) }
            }
        }

    }
}