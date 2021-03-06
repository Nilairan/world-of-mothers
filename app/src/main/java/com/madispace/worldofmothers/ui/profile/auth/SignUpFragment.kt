package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.databinding.FragmentSignUpBinding
import com.madispace.worldofmothers.routing.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpFragment : ObserveFragment<SignUpViewModel>(SignUpViewModel::class.java) {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.register)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.existAccountButton.setOnClickListener { onBackPressed() }
        binding.loginButton.setOnClickListener {
            viewModel.obtainEvent(SignUpViewModel.SignUpEvent.ValidateFields)
        }
        observeEditText()
    }

    override fun initObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.viewActions().collect { action -> action?.let { bindAction(it) } }
            viewModel.viewStates().collect { action -> action?.let { bindStates(it) } }
        }
    }

    private fun bindAction(action: SignUpViewModel.SignUpAction) {
        with(binding) {
            when (action) {
                is SignUpViewModel.SignUpAction.NameNotValid -> nameLayout.error =
                        getString(R.string.name_not_valid)
                is SignUpViewModel.SignUpAction.EmailNotValid -> emailLayout.error =
                        getString(R.string.email_not_valid)
                is SignUpViewModel.SignUpAction.PhoneNotValid -> phoneLayout.error =
                        getString(R.string.phone_not_valid)
                is SignUpViewModel.SignUpAction.PasswordNotValid -> passLayout.error =
                        getString(R.string.pass_not_valid)
                is SignUpViewModel.SignUpAction.RepeatPasswordNotValid -> repeatPassLayout.error =
                        getString(R.string.repeate_pess_not_valid)
            }
        }
    }

    private fun bindStates(event: SignUpViewModel.SignUpState) {
        when (event) {
            is SignUpViewModel.SignUpState.NoValidFields -> {
            }
        }
    }

    private fun observeEditText() {
        with(binding) {
            nameLayout.editText?.doAfterTextChanged { name ->
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetName(
                        value = name?.let { it.toString() } ?: ""))
            }
            phoneLayout.editText?.doAfterTextChanged { phone ->
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetPhone(
                        value = phone?.let { it.toString() } ?: ""))
            }
            emailLayout.editText?.doAfterTextChanged { email ->
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetEmail(
                        value = email?.let { it.toString() } ?: ""))
            }
            passLayout.editText?.doAfterTextChanged { pass ->
                viewModel.obtainEvent(SignUpViewModel.SignUpEvent.SetPassword(
                        value = pass?.let { it.toString() } ?: ""))
            }
            repeatPassLayout.editText?.doAfterTextChanged { repeatPass ->
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