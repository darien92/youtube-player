package com.darien.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darien.core.redux.Action
import com.darien.core.redux.State
import com.darien.core.redux.Store
import kotlinx.coroutines.*

open class BaseViewModel<S: State, A: Action> (
    private val store: Store<S, A>,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val uiState = store.state

    private val viewModelSafeScope by lazy {
        viewModelScope + dispatcher + coroutineExceptionHandler
    }

    private val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            handleError(ex)
        }
    }

    /**
     * Allows to handle coroutines errors
     */
    open fun handleError(exception: Throwable) {
        Log.e("BaseViewModel",
            "CoroutineExceptionHandler: ${exception.localizedMessage}",
            exception
        )
    }

    protected fun handleAction(action: A) {
        viewModelSafeScope.launch {
            store.dispatch(action)
        }
    }
}