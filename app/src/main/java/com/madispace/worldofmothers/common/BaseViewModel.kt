package com.madispace.worldofmothers.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent

abstract class BaseViewModel : ViewModel(), LifecycleObserver, KoinComponent {

    fun addLifecycleObserver(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {

    }

}