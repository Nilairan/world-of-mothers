package com.madispace.worldofmothers.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 2/3/21
 */
abstract class BaseMviViewModel<STATE, ACTION, EVENT> : BaseViewModel() {

    private val TAG = BaseMviViewModel::class.java.simpleName
    private val _viewStates: MutableStateFlow<STATE?> = MutableStateFlow(null)
    fun viewStates(): StateFlow<STATE?> = _viewStates

    protected var viewState: STATE
        get() = _viewStates.value
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            /** StateFlow doesn't work with same values */
            if (_viewStates.value == value) {
                _viewStates.value = null
            }

            _viewStates.value = value
        }

    private val _viewActions: MutableStateFlow<ACTION?> = MutableStateFlow(null)
    fun viewActions(): StateFlow<ACTION?> = _viewActions

    protected var viewAction: ACTION
        get() = _viewActions.value
            ?: throw UninitializedPropertyAccessException("\"viewAction\" was queried before being initialized")
        set(value) {
            /** StateFlow doesn't work with same values */
            if (_viewActions.value == value) {
                _viewActions.value = null
            }

            _viewActions.value = value
        }

    abstract fun obtainEvent(viewEvent: EVENT)
}