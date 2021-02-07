package com.madispace.worldofmothers.common

import android.content.Context
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.*

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
fun ViewBinding.getContext(): Context {
    return this.root.context
}

fun ImageView.loadPhoto(url: String) {
    if (url.isEmpty()) return
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(this)
}

fun Double.getPrice(): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    numberFormat.maximumFractionDigits = 2
    return numberFormat.format(this)
}