package com.madispace.worldofmothers.ui.profile.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.databinding.FragmentNewProductBinding

class NewProductFragment : Fragment() {

    private lateinit var binding: FragmentNewProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewProductBinding.inflate(inflater, container, false)
        binding.appBar.toolbarName.text = getString(R.string.add_product_title)
        return binding.root
    }
//
//    override fun initObservers() {
//    }

    companion object {
        fun newInstance() = NewProductFragment()
    }
}