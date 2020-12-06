package com.madispace.worldofmothers.ui.profile.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.debounceClicks
import com.madispace.worldofmothers.databinding.FragmentSignUpBinding
import com.madispace.worldofmothers.routing.Screens

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/4/20
 */
class SignUpFragment : ObserveFragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.register)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.existAccountButton.debounceClicks().subscribe { onBackPressed() }
        binding.loginButton.debounceClicks().subscribe {
            viewModel.auth()
            router.replaceScreen(Screens.ProfileScreen())
        }
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