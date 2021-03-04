package com.madispace.worldofmothers.common

sealed class Event<T>
class Success<T>(val data: T) : Event<T>()
class Error<T>(val message: String = "", val errorRes: Int = 0) : Event<T>()
class Loading<T>() : Event<T>()
