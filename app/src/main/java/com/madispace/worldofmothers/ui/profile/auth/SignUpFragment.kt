package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.databinding.FragmentSignUpBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.common.*

class SignUpFragment : ObserveFragment<SignUpViewModel>(SignUpViewModel::class.java) {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.register)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.existAccountButton.debounceClicks().subscribe { onBackPressed() }
//        binding.nameEditText.textChanges().subscribeBy { viewModel.name = it.toString() }
//        binding.phoneEditText.textChanges().subscribeBy { viewModel.phone = it.toString() }
//        binding.emailEditText.textChanges().subscribeBy { viewModel.email = it.toString() }
//        binding.passEditText.textChanges().subscribeBy { viewModel.pass = it.toString() }
//        binding.repeatPassEditText.textChanges().subscribeBy { viewModel.repeatPass = it.toString() }
//        binding.loginButton.debounceClicks().subscribe {
//            viewModel.auth()
//            router.replaceScreen(Screens.ProfileScreen())
//        }
    }

    override fun onBackPressed(): Boolean {
        router.replaceScreen(Screens.SignInScreen())
        return true
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
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}