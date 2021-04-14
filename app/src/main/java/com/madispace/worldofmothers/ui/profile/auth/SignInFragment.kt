package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.common.launchWhenStarted
import com.madispace.worldofmothers.databinding.FragmentSignInBinding
import com.madispace.worldofmothers.routing.Screens
import kotlinx.coroutines.flow.onEach

class SignInFragment : ObserveFragment<SignInViewModel>(
    SignInViewModel::class.java,
    R.layout.fragment_sign_in
) {

    private val binding: FragmentSignInBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBar.toolbarName.text = getString(R.string.sign_in)
            registerButton.setOnClickListener { router.replaceScreen(Screens.SignUpScreen()) }
            loginEditText.doAfterTextChanged {
                loginLayout.error = null
                loginLayout.isErrorEnabled = false
                viewModel.obtainEvent(SignInViewModel.SignInEvent.SetEmail(it.toString()))
            }
            passEditText.doAfterTextChanged {
                passLayout.error = null
                passLayout.isErrorEnabled = false
                viewModel.obtainEvent(SignInViewModel.SignInEvent.SetPassword(it.toString()))
            }
            loginButton.setOnClickListener {
                viewModel.obtainEvent(SignInViewModel.SignInEvent.Login)
            }
        }
    }

    override fun initObservers() {
        viewModel.viewStates().onEach { states ->
            states?.let { bindViewState(it) }
        }.launchWhenStarted(lifecycleScope)
        viewModel.viewActions().onEach { actions ->
            actions?.let { bindViewAction(it) }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun bindViewState(state: SignInViewModel.SignInState) {
        when (state) {
            is SignInViewModel.SignInState.SuccessAuth -> router.replaceScreen(Screens.ProfileScreen())
        }
    }

    private fun bindViewAction(action: SignInViewModel.SignInAction) {
        when (action) {
            is SignInViewModel.SignInAction.EmailNotValid -> binding.loginLayout.error =
                getString(R.string.email_not_valid)
            is SignInViewModel.SignInAction.PassNotValid -> binding.passLayout.error =
                getString(R.string.pass_not_valid)
            is SignInViewModel.SignInAction.BadFields -> Toast.makeText(binding.getContext(),
                getString(R.string.auth_bad_fields), Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return false
    }

    companion object {
        fun newInstance() = SignInFragment()
    }
}