package com.madispace.worldofmothers.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
abstract class ObserveFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    abstract fun initObservers()
}