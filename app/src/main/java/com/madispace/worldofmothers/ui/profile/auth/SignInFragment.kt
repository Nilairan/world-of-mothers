package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.databinding.FragmentSignInBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.common.*

class SignInFragment : ObserveFragment<SignInViewModel>(SignInViewModel::class.java) {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.sign_in)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerButton.setOnClickListener { router.replaceScreen(Screens.SignUpScreen()) }
//        binding.loginEditText.textChanges().subscribeBy { viewModel.email = it.toString() }
//        binding.passEditText.textChanges().subscribeBy { viewModel.pass = it.toString() }
//        binding.loginButton.debounceClicks().subscribeBy {
//            viewModel.auth()
//            router.replaceScreen(Screens.ProfileScreen())
//        }
    }

    override fun initObservers() {
        viewModel.validUiModel.observe(viewLifecycleOwner, object : SimpleObserver<UiModel>() {
            override fun onSuccess(data: UiModel) {
                when (data) {
                    is FiledValid -> {
                        binding.loginButton.isEnabled = true
                    }
                    is EmailInvalid -> {
                        binding.loginButton.isEnabled = false
                    }
                    is PassInvalid -> {
                        binding.loginButton.isEnabled = false
                    }
                    is Default -> {
                        binding.loginButton.isEnabled = false
                        binding.loginLayout.error = null
                        binding.passLayout.error = null
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance() = SignInFragment()
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return false
    }
}