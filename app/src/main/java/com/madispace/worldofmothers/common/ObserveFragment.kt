package com.madispace.worldofmothers.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import com.github.terrakok.cicerone.Router
import com.madispace.worldofmothers.routing.RouterProvider
import kotlin.reflect.KClass

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
abstract class ObserveFragment<VM : BaseViewModel>(private val viewModelClass: KClass<VM>) :
    Fragment(), RouterProvider, BackButtonListener {

    val viewModel: VM by lazy { getViewModel().value }

    private fun getViewModel(): Lazy<VM> {
        return createViewModelLazy(viewModelClass, { viewModelStore }, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addLifecycleObserver(lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    override val router: Router
        get() = (parentFragment as RouterProvider).router

    abstract fun initObservers()
}