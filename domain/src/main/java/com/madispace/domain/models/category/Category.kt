package com.madispace.domain.models.category

data class Category(
        val id: Int,
        val name: String,
        val subcategories: List<Subcategories>
)

data class Subcategories(
        val id: Int,
        val name: String
)
