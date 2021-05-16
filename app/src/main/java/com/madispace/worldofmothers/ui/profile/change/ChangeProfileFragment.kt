package com.madispace.worldofmothers.ui.profile.change

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.domain.models.user.Profile
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.addPhoneMaskTextListener
import com.madispace.worldofmothers.common.launchWhenStarted
import com.madispace.worldofmothers.databinding.FragmentChangeProfileBinding
import kotlinx.coroutines.flow.onEach

class ChangeProfileFragment : ObserveFragment<ChangeProfileViewModel>(
    ChangeProfileViewModel::class.java,
    R.layout.fragment_change_profile
) {

    private val binding: FragmentChangeProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBar.toolbarName.text = getString(R.string.change_profile)
            saveButton.setOnClickListener {
                viewModel.obtainEvent(ChangeProfileViewModel.ChangeProfileEvent.ChangeProfile)
            }
            nameLayout.editText?.doAfterTextChanged { name ->
                nameLayout.error = null
                nameLayout.isErrorEnabled = false
                viewModel.obtainEvent(
                    ChangeProfileViewModel.ChangeProfileEvent.SetName(
                        value = name?.toString() ?: ""
                    )
                )
            }
            surnameLayout.editText?.doAfterTextChanged { name ->
                surnameLayout.error = null
                surnameLayout.isErrorEnabled = false
                viewModel.obtainEvent(
                    ChangeProfileViewModel.ChangeProfileEvent.SetSurname(
                        value = name?.toString() ?: ""
                    )
                )
            }
            phoneLayout.editText?.addPhoneMaskTextListener { phone ->
                phoneLayout.error = null
                phoneLayout.isErrorEnabled = false
                viewModel.obtainEvent(
                    ChangeProfileViewModel.ChangeProfileEvent.SetPhone(
                        value = phone
                    )
                )
            }
        }
    }

    override fun initObservers() {
        viewModel.viewActions()
            .onEach { action -> action?.let { bindAction(action) } }
            .launchWhenStarted(lifecycleScope)
        viewModel.viewStates()
            .onEach { state -> state?.let { bindState(state) } }
            .launchWhenStarted(lifecycleScope)
    }

    private fun bindState(state: ChangeProfileViewModel.ChangeProfileState) {
        when (state) {
            is ChangeProfileViewModel.ChangeProfileState.Loading -> binding.progressBar.root.isVisible =
                state.loading
            is ChangeProfileViewModel.ChangeProfileState.ShowProfile -> showProfile(state.profile)
        }
    }

    private fun bindAction(action: ChangeProfileViewModel.ChangeProfileAction) {
        when (action) {
            is ChangeProfileViewModel.ChangeProfileAction.ShowError ->
                Toast.makeText(context, action.error, Toast.LENGTH_SHORT).show()
            is ChangeProfileViewModel.ChangeProfileAction.NameNotValid -> binding.nameLayout.error =
                getString(R.string.field_not_valid)
            is ChangeProfileViewModel.ChangeProfileAction.SurnameNotValid -> binding.surnameLayout.error =
                getString(R.string.field_not_valid)
            is ChangeProfileViewModel.ChangeProfileAction.PhoneNotValid -> binding.phoneLayout.error =
                getString(R.string.phone_not_valid)
        }
    }

    private fun showProfile(profile: Profile) {
        with(binding) {
            nameEditText.setText(profile.firstName)
            surnameLayout.editText?.setText(profile.surname)
            phoneEditText.setText(profile.tel)
        }
    }

    companion object {
        fun newInstance() = ChangeProfileFragment()
    }
}