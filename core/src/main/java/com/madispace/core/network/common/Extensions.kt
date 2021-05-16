package com.madispace.core.network.common

import com.google.gson.Gson
import com.madispace.core.network.dto.ApiError
import retrofit2.HttpException

fun HttpException.getApiError(): ApiError {
    return Gson().fromJson(this.response()?.errorBody()?.charStream(), ApiError::class.java)
}