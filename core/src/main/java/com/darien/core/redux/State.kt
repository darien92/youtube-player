package com.darien.core.redux

/**
 * Just to define states type
 */
interface State {
    var isLoading: Boolean
    val error: DomainError?
}