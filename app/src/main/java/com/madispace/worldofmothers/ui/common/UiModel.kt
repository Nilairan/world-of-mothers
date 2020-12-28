package com.madispace.worldofmothers.ui.common

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/24/20
 */
sealed class UiModel
object EmailInvalid : UiModel()
object PassInvalid : UiModel()
object FiledValid : UiModel()
object Default : UiModel()
