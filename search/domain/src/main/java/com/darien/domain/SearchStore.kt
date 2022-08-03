package com.darien.domain

import com.darien.core.redux.Store
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
