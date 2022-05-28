package com.darien.core.redux

/**
 * creates a new state based on a current state and an action
 */
interface Reducer<S: State, A: Action> {
    suspend fun reduce(prevState: S, action: A): S
}