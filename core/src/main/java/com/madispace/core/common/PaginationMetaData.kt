package com.madispace.core.common

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/6/21
 */
data class PaginationMetaData(
    val totalCount: Int,
    val pageCount: Int,
    val currentPage: Int,
    val perPage: Int
)
