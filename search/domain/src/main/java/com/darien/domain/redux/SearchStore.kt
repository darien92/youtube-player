package com.darien.domain.redux

import com.darien.core.redux.Store
import com.darien.domain.data.SearchActions
import com.darien.domain.data.SearchViewState
import javax.inject.Inject

class SearchStore @Inject constructor(reducer: SearchReducer) :
    Store<SearchViewState, SearchActions>(
        initialState = SearchViewState(
            error = null,
            isLoading = false,
            word = "",
            selectedWord = "",
            currWords = ArrayList()
        ), reducer = reducer
    )
