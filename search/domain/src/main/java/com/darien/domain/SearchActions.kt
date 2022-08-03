package com.darien.domain

import com.darien.core.redux.Action
import com.darien.core.redux.DomainError

sealed class SearchActions: Action {
    data class Error(val error: DomainError): SearchActions()
    data class WordTyped(val word: String): SearchActions()
    data class WordSelected(val word: String): SearchActions()
    data class NewWord(val wordId: Long, val word: String): SearchActions()
    object StartLoading: SearchActions()
    object StopLoading: SearchActions()
}