package com.darien.data.models

import java.util.*

data class Word(
    val id: Long = UUID.randomUUID().timestamp(),
    val word: String,
    val timestamp: Long = System.currentTimeMillis()
)