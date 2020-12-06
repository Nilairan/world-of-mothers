package com.madispace.worldofmothers.ui.profile.change

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.databinding.FragmentChangeProfileBinding

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/6/20
 */
class ChangeProfileFragment : ObserveFragment() {

    private lateinit var binding: FragmentChangeProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChangeProfileBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.change_profile)
        return binding.root
    }

    override fun initObservers() {
    }

    companion object {
        fun newInstance() = ChangeProfileFragment()
    }
}