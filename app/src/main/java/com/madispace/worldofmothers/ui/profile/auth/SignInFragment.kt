package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.debounceClicks
import com.madispace.worldofmothers.databinding.FragmentSignInBinding
import com.madispace.worldofmothers.routing.Screens


/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
class SignInFragment : ObserveFragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.sign_in)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerButton.debounceClicks().subscribe { router.replaceScreen(Screens.SignUpScreen()) }
    }

    override fun initObservers() {

    }

    companion object {
        fun newInstance() = SignInFragment()
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}