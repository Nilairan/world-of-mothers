package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.widget.textChanges
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.common.debounceClicks
import com.madispace.worldofmothers.databinding.FragmentSignInBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.common.*
import io.reactivex.rxjava3.kotlin.subscribeBy


/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
class SignInFragment : ObserveFragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.sign_in)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerButton.debounceClicks()
            .subscribe { router.replaceScreen(Screens.SignUpScreen()) }
        binding.loginEditText.textChanges().subscribeBy { viewModel.email = it.toString() }
        binding.passEditText.textChanges().subscribeBy { viewModel.pass = it.toString() }
        binding.loginButton.debounceClicks().subscribeBy {
            viewModel.auth()
            router.replaceScreen(Screens.ProfileScreen())
        }
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
        return true
    }
}