package com.madispace.worldofmothers.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.terrakok.cicerone.Screen
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.databinding.FragmentProfileBinding
import com.madispace.worldofmothers.routing.RouterProvider

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/30/20
 */
class ProfileFragment : ObserveFragment(), RouterProvider {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.addLifecycleObserver(lifecycle)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    override fun initObservers() {
        viewModel.routingLiveData.observe(viewLifecycleOwner, object : SimpleObserver<Screen>() {
            override fun onSuccess(data: Screen) {
                router.navigateTo(data)
            }
        })
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}