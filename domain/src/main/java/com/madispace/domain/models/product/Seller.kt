package com.madispace.domain.models.product

data class Seller(
    val username: String,
    val firstName: String,
    val surname: String,
    val tel: String,
    val image: String,
    val itemsCount: Int
)
