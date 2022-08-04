package com.darien.ui.viewmodels

import com.darien.core.viewmodel.BaseViewModel
import com.darien.domain.data.SearchActions
import com.darien.domain.redux.SearchStore
import com.darien.domain.data.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    store: SearchStore,
    dispatcher: CoroutineDispatcher
) :
    BaseViewModel<SearchViewState, SearchActions>(store, dispatcher = dispatcher) {
    fun onWordTyped(word: String) {
        handleAction(SearchActions.WordTyped(word = word))
    }

    fun onSelectedWord(word: String) {
        handleAction(SearchActions.WordSelected(word = word))
    }

    fun onNewWord(wordId: Long = System.currentTimeMillis(), word: String) {
        handleAction(SearchActions.NewWord(wordId = wordId, word = word))
    }
}