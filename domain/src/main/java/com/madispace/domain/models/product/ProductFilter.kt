package com.madispace.domain.models.product

sealed class ProductFilter {
    object Default : ProductFilter()
    data class Category(val id: Int) : ProductFilter()
    data class Filtered(
        val min: Int?,
        val max: Int?,
        val isNew: Boolean?
    ) : ProductFilter()

    data class Search(val value: String) : ProductFilter()
}
