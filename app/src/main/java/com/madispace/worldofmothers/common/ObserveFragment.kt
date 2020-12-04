package com.madispace.worldofmothers.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import com.madispace.worldofmothers.routing.RouterProvider

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
abstract class ObserveFragment : Fragment(), RouterProvider, BackButtonListener {

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