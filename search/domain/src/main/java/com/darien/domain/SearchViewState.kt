package com.darien.domain

import com.darien.core.redux.DomainError
import com.darien.core.redux.State
import com.darien.data.models.Word

data class SearchViewState(
    override val error: DomainError?,
    override var isLoading: Boolean,
    val word: String,
    val selectedWord: String = "",
    val currWords: MutableList<Word> = emptyList<Word>().toMutableList()
): State
