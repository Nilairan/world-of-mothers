package com.madispace.core.common

interface Mapper<T, R> {
    fun map(item: T): R
}