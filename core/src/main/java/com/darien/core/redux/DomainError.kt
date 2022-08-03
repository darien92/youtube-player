package com.darien.core.redux

data class DomainError(
    val errorCode: Int = 0,
    val errorMessage: String
)
