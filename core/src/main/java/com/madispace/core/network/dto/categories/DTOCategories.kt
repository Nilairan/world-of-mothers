package com.madispace.core.network.dto.categories

import com.madispace.domain.models.category.Category
import com.madispace.domain.models.category.Subcategories

data class DTOCategories(
        val id: Int,
        val name: String,
        val subcategories: List<DTOSubcategories>
) {
    fun mapToModel(): Category {
        return Category(
                id = id,
                name = name,
                subcategories = subcategories.map { it.mapToModel() }
        )
    }

}

data class DTOSubcategories(
        val id: Int,
        val name: String
) {
    fun mapToModel(): Subcategories {
        return Subcategories(
                id = id,
                name = name
        )
    }
}
