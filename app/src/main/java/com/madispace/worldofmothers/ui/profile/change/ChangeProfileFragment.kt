package com.madispace.worldofmothers.ui.profile.change

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.databinding.FragmentChangeProfileBinding

class ChangeProfileFragment : Fragment(R.layout.fragment_change_profile) {

    private val binding: FragmentChangeProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarName.text = getString(R.string.change_profile)
    }

    //
//    override fun initObservers() {
//    }

    companion object {
        fun newInstance() = ChangeProfileFragment()
    }
}