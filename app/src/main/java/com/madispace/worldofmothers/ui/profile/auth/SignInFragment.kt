package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.databinding.FragmentSignInBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.common.*

class SignInFragment : ObserveFragment<SignInViewModel>(
    SignInViewModel::class.java,
    R.layout.fragment_sign_in
) {

    private val binding: FragmentSignInBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarName.text = getString(R.string.sign_in)
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

    override fun onBackPressed(): Boolean {
        router.exit()
        return false
    }

    companion object {
        fun newInstance() = SignInFragment()
    }
}