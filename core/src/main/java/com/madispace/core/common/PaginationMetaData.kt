package com.madispace.core.common

data class PaginationMetaData(
    val totalCount: Int,
    val pageCount: Int,
    val currentPage: Int,
    val perPage: Int
)
