package com.madispace.worldofmothers.ui.common

sealed class UiModel
object EmailInvalid : UiModel()
object PassInvalid : UiModel()
object FiledValid : UiModel()
object Default : UiModel()
