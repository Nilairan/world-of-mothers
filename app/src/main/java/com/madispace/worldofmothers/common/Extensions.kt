package com.madispace.worldofmothers.common

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
fun ViewBinding.getContext(): Context {
    return this.root.context
}

fun View.debounceClicks(): Observable<Unit> {
    return this.clicks()
            .debounce(250, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.subscribeIo(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Observable<T>.observeUi(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}