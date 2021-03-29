package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.addPhoneMaskTextListener
import com.madispace.worldofmothers.common.launchWhenStarted
import com.madispace.worldofmothers.databinding.FragmentSignUpBinding
import com.madispace.worldofmothers.routing.Screens
import kotlinx.coroutines.flow.onEach

class SignUpFragment : ObserveFragment<SignUpViewModel>(
    SignUpViewModel::class.java,
    R.layout.fragment_sign_up
) {

    private val binding: FragmentSignUpBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarName.text = getString(R.string.register)
        binding.existAccountButton.setOnClickListener { onBackPressed() }
        binding.loginButton.setOnClickListener {
            viewModel.obtainEvent(SignUpViewModel.SignUpEvent.ValidateFields)
        }
        observeEditText()
    }

    override fun initObservers() {
        viewModel.viewActions().onEach { action ->
            action?.let { bindAction(it) }
        }.launchWhenStarted(lifecycleScope)

        viewModel.viewStates().onEach { action ->
            action?.let { bindStates(it) }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun bindAction(action: SignUpViewModel.SignUpAction) {
        with(binding) {
            when (action) {
                is SignUpViewModel.SignUpAction.NameNotValid -> nameLayout.error =
                    getString(R.string.field_not_valid)
                is SignUpViewModel.SignUpAction.SurnameNotValid -> surnameLayout.error =
                    getString(R.string.field_not_valid)
                is SignUpViewModel.SignUpAction.EmailNotValid -> emailLayout.error =
                    getString(R.string.email_not_valid)
                is SignUpViewModel.SignUpAction.PhoneNotValid -> phoneLayout.error =
                    getString(R.string.phone_not_valid)
                is SignUpViewModel.SignUpAction.PasswordNotValid -> passLayout.error =
                    getString(R.string.pass_not_valid)
                is SignUpViewModel.SignUpAction.RepeatPasswordNotValid -> repeatPassLayout.error =
                    getString(R.string.repeate_pess_not_valid)
                is SignUpViewModel.SignUpAction.EmailIsBusy -> emailLayout.error =
                    getString(R.string.email_is_busy)
                is SignUpViewModel.SignUpAction.UserDataNoValid -> {
                    /*TODO use alert*/
                }
                is SignUpViewModel.SignUpAction.SuccessRegisterUser -> router.replaceScreen(Screens.ProfileScreen())
            }
        }
    }

    private fun bindStates(event: SignUpViewModel.SignUpState) {
        with(binding) {
            when (event) {
                is SignUpViewModel.SignUpState.NoValidFields -> {
                }
                is SignUpViewModel.SignUpState.ShowLoading -> {
                    progressBar.root.visibility = View.VISIBLE
                }
                is SignUpViewModel.SignUpState.HideLoading -> {
                    progressBar.root.visibility = View.GONE
                }
            }
        }
    }

    private fun observeEditText() {
        with(binding) {
            nameLayout.editText?.doAfterTextChanged { name ->
                nameLayout.error = null
                nameLayout.isErrorEnabled = false
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetName(
                    value = name?.let { it.toString() } ?: ""))
            }
            surnameLayout.editText?.doAfterTextChanged { name ->
                surnameLayout.error = null
                surnameLayout.isErrorEnabled = false
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetSurname(
                    value = name?.let { it.toString() } ?: ""))
            }
            phoneLayout.editText?.addPhoneMaskTextListener { phone ->
                phoneLayout.error = null
                phoneLayout.isErrorEnabled = false
                viewModel.obtainEvent(
                    SignUpViewModel.SignUpEvent.SetPhone(
                        value = phone
                    )
                )
            }
            emailLayout.editText?.doAfterTextChanged { email ->
                emailLayout.error = null
                emailLayout.isErrorEnabled = false
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetEmail(
                    value = email?.let { it.toString() } ?: ""))
            }
            passLayout.editText?.doAfterTextChanged { pass ->
                passLayout.error = null
                passLayout.isErrorEnabled = false
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetPassword(
                    value = pass?.let { it.toString() } ?: ""))
            }
            repeatPassLayout.editText?.doAfterTextChanged { repeatPass ->
                repeatPassLayout.error = null
                repeatPassLayout.isErrorEnabled = false
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetRepeatPassword(
                    value = repeatPass?.let { it.toString() } ?: ""))
            }
        }
    }

    override fun onBackPressed(): Boolean {
        router.replaceScreen(Screens.SignInScreen())
        return true
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}