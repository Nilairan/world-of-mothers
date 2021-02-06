package com.madispace.worldofmothers.common

import android.content.Context
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
fun ViewBinding.getContext(): Context {
    return this.root.context
}

fun ImageView.loadPhoto(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(this)
}