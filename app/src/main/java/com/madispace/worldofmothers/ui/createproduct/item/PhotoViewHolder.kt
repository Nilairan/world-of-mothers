package com.madispace.worldofmothers.ui.createproduct.item

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.databinding.ItemImageBinding
import com.xwray.groupie.viewbinding.BindableItem

class PhotoViewHolder(
    private val map: Pair<Uri, Bitmap>,
    private val removeClickListener: (Uri) -> Unit
) : BindableItem<ItemImageBinding>() {
    override fun bind(viewBinding: ItemImageBinding, position: Int) {
        with(viewBinding) {
            photoImage.setImageBitmap(map.second)
            deleteContainer.setOnClickListener { removeClickListener.invoke(map.first) }
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_image
    }

    override fun initializeViewBinding(view: View): ItemImageBinding {
        return ItemImageBinding.bind(view)
    }
}