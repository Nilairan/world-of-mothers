package com.madispace.domain.exceptions

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/24/20
 */
class CustomFunctionException(
        val field: String
) : RuntimeException()