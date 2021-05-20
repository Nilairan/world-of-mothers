package com.madispace.worldofmothers.common

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.madispace.worldofmothers.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.io.File
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
        .placeholder(R.drawable.ic_photo_placeholder)
        .into(this)
}

fun Double.getPrice(): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    numberFormat.maximumFractionDigits = 2
    return numberFormat.format(this)
}

fun EditText.addPhoneMaskTextListener(phoneListener: (String) -> Unit) {
    this.addTextChangedListener(
        MaskedTextChangedListener.installOn(
            editText = this,
            primaryFormat = "+7 ([000]) [000]-[00]-[00]",
            autoskip = true,
            autocomplete = false,
            valueListener = object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    phoneListener.invoke(extractedValue)
                }
            }
        ))
}

fun <T> Flow<T>.launchWhenStarted(scope: LifecycleCoroutineScope) {
    scope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}

fun <T> Flow<T>.catchWithLog(throwableBlock: (Throwable) -> Unit): Flow<T> {
    return this.catch {
        it.printStackTrace()
        throwableBlock.invoke(it)
    }
}


fun Context.createFile(): File {
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "file_",
        ".jpg",
        storageDir
    )
}

fun Context.getUriByFile(file: File): Uri {
    return FileProvider.getUriForFile(this, "com.madispace.worldofmothers.fileprovider", file)
}

fun TextInputLayout.doAfterTextChanged(block: (String) -> Unit) {
    error = null
    isErrorEnabled = false
    editText?.doAfterTextChanged {
        block.invoke(it?.toString() ?: "")
    }
}