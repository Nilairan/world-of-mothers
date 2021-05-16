package com.madispace.core.network.dto

data class ApiError(
    val name: String,
    val message: String,
    val code: Int,
    val status: Int
)
