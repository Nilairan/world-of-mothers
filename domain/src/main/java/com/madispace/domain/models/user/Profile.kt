package com.madispace.domain.models.user

data class Profile(
        val username: String,
        val firstName: String,
        val surname: String,
        val tel: String,
        val image: String,
        val itemsCount: String
)