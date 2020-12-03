package com.madispace.worldofmothers.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.core.component.KoinComponent

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/1/20
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver, KoinComponent {

    protected val compositeDisposable = CompositeDisposable()

    fun addLifecycleObserver(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onDestroy() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun dispose() {
        compositeDisposable.clear()
    }

}