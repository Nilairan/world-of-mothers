package com.madispace.worldofmothers.common

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
sealed class Event<T>
class Success<T>(val data: T) : Event<T>()
class Error<T>(val message: String = "", val errorRes: Int = 0) : Event<T>()
class Loading<T>() : Event<T>()
