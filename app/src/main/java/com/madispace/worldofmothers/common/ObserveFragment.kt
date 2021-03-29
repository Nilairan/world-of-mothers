package com.madispace.worldofmothers.common

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import com.madispace.worldofmothers.routing.RouterProvider
import org.koin.android.viewmodel.compat.ViewModelCompat.viewModel

abstract class ObserveFragment<VM : BaseViewModel>(
    viewModelClass: Class<VM>,
    @LayoutRes view: Int
) :
    Fragment(view), RouterProvider, BackButtonListener {

    val viewModel: VM by viewModel(this, viewModelClass)

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