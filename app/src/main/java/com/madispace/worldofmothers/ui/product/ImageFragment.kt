package com.madispace.worldofmothers.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.args
import com.madispace.worldofmothers.common.loadPhoto
import com.madispace.worldofmothers.databinding.FragmentImageBinding

class ImageFragment : Fragment(R.layout.fragment_image) {

    private var imageUrl: String by args()
    private val binding: FragmentImageBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.loadPhoto(imageUrl)
    }

    companion object {
        fun newInstance(imageUrl: String) = ImageFragment().apply {
            this.imageUrl = imageUrl
        }
    }
}