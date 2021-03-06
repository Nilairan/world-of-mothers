package com.madispace.worldofmothers.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import java.text.NumberFormat
import java.util.*

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

fun EditText.collect(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                offer(s)
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}