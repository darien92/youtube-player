package com.darien.core.redux

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A [Store] is our state container for a given screen.
 *
 * @param[initialState] This is the initial state of the screen when it is first created.
 * @param[reducer] A system for taking in the current state, and a new action, and outputting the
 * updated state.
 */
open class Store<S : State, A : Action>(
    initialState: S,
    private val reducer: Reducer<S, A>
) {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val currentState: S
        get() = _state.value

    suspend fun dispatch(action: A) {
        val newState = reducer.reduce(currentState, action)
        _state.value = newState
    }
}