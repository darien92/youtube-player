package com.darien.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darien.core.redux.Action
import com.darien.core.redux.Reducer
import com.darien.core.redux.State
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel<S: State, A: Action> (
    private val reducer: Reducer<S, A>,
    initialState: S,
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    protected val viewModelSafeScope by lazy {
        viewModelScope + coroutineExceptionHandler
    }

    private val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            handleError(ex)
        }
    }

    open fun handleError(exception: Throwable) {
        Log.e("BaseViewModel",
            "CoroutineExceptionHandler: ${exception.localizedMessage}",
            exception
        )
    }

    protected fun stopJob() {
        if(viewModelSafeScope.isActive) {
            viewModelSafeScope.cancel()
        }
    }

    protected fun handleAction(action: A) {
        viewModelSafeScope.launch {
            _uiState.value = reducer.reduce(_uiState.value, action)
        }
    }
}