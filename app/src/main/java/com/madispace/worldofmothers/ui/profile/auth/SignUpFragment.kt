package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.databinding.FragmentSignUpBinding
import com.madispace.worldofmothers.routing.Screens

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
class SignUpFragment : ObserveFragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.register)
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        router.replaceScreen(Screens.SignInScreen())
        return true
    }

    override fun initObservers() {
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}