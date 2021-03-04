package com.madispace.worldofmothers.ui.profile.change

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.databinding.FragmentChangeProfileBinding

class ChangeProfileFragment : Fragment() {

    private lateinit var binding: FragmentChangeProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeProfileBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.change_profile)
        return binding.root
    }
//
//    override fun initObservers() {
//    }

    companion object {
        fun newInstance() = ChangeProfileFragment()
    }
}