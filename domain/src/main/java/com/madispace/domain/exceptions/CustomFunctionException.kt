package com.madispace.domain.exceptions

class CustomFunctionException(
        val field: String
) : RuntimeException()