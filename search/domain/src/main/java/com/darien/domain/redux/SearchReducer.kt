package com.darien.domain.redux

import com.darien.core.redux.Reducer
import com.darien.data.models.Word
import com.darien.data.repositories.SearchWordRepository
import com.darien.domain.data.SearchActions
import com.darien.domain.data.SearchViewState
import javax.inject.Inject

class SearchReducer @Inject constructor(private val repository: SearchWordRepository) :
    Reducer<SearchViewState, SearchActions> {
    override suspend fun reduce(
        prevState: SearchViewState,
        action: SearchActions
    ): SearchViewState {
        return when (action) {
            is SearchActions.StartLoading -> {
                prevState.copy(error = null, isLoading = true)
            }
            is SearchActions.StopLoading -> {
                prevState.copy(isLoading = false)
            }
            is SearchActions.WordTyped -> {
                val matchingWords = repository.getWords(action.word)
                prevState.copy(
                    selectedWord = "",
                    word = action.word,
                    currWords = matchingWords.toMutableList()
                )
            }
            is SearchActions.WordSelected -> {
                prevState.copy(selectedWord = action.word)
            }
            is SearchActions.NewWord -> {
                repository.saveWord(Word(id = action.wordId, word = action.word))
                prevState.copy(selectedWord = action.word, word = action.word)
            }
            is SearchActions.Error -> {
                prevState.copy(isLoading = false, error = action.error)
            }
        }
    }
}