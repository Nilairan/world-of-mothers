package com.madispace.worldofmothers.ui.profile.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.databinding.FragmentNewProductBinding

class NewProductFragment : Fragment(R.layout.fragment_new_product) {

    private val binding: FragmentNewProductBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarName.text = getString(R.string.add_product_title)
    }
//
//    override fun initObservers() {
//    }

    companion object {
        fun newInstance() = NewProductFragment()
    }
}