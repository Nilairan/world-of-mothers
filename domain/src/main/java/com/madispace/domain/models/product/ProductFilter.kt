package com.madispace.domain.models.product

sealed class ProductFilter {
    data class Category(val id: Int) : ProductFilter()
    data class Min(val value: Double) : ProductFilter()
    data class Max(val value: Double) : ProductFilter()
    data class Search(val value: String) : ProductFilter()
    object Desc : ProductFilter()
    object Asc : ProductFilter()
}
