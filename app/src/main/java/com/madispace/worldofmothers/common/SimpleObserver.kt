package com.madispace.worldofmothers.common

import androidx.lifecycle.Observer

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
abstract class SimpleObserver<T> : Observer<Event<T>> {

    override fun onChanged(item: Event<T>) {
        when (item) {
            is Success -> {
                onLoading(loading = false)
                onSuccess(data = item.data)
            }
            is Error -> {
                onLoading(loading = false)
                when {
                    item.message.isNotEmpty() -> onError(errorMessage = item.message)
                    item.errorRes != 0 -> onError(errorRes = item.errorRes)
                    else -> onError(errorMessage = item.message)
                }
            }
            is Loading -> onLoading(loading = true)
        }
    }

    open fun onSuccess(data: T) {}
    open fun onError(errorRes: Int) {}
    open fun onError(errorMessage: String) {}
    open fun onLoading(loading: Boolean) {}

}