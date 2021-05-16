package com.madispace.domain.models.image

data class PhotoModel(
    val file: ByteArray,
    val mediaType: String,
    val fileName: String
)
