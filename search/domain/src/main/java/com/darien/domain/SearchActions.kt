package com.darien.domain

import com.darien.core.redux.Action

sealed class SearchActions: Action {
    data class WordTyped(val word: String): SearchActions()
    data class WordSelected(val word: String): SearchActions()
    data class NewWord(val wordId: Long, val word: String): SearchActions()
    object StartLoading: SearchActions()
    object StopLoading: SearchActions()
}