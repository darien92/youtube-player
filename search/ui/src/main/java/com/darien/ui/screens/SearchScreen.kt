package com.darien.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.darien.core.navigation.YoutubePlayerScreens
import com.darien.ui.R
import com.darien.ui.components.NoDataView
import com.darien.ui.components.SearchBar
import com.darien.ui.components.WordResultContainer
import com.darien.ui.viewmodels.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {

        SearchBar(
            label = stringResource(id = R.string.search),
            value = state.word,
            keyboardAction = KeyboardActions(
                onSearch = {
                    viewModel.onNewWord(word = state.word)
                }
            )
        ) {
            viewModel.onWordTyped(word = it)
        }
        if (state.selectedWord.trim().isNotEmpty()) {
            navController.navigate(YoutubePlayerScreens.VideosScreen.name + "/" + state.selectedWord)
            viewModel.onWordTyped(word = "")
        } else if (state.word.trim().isEmpty() || state.currWords.isEmpty()) {
            NoDataView()
        } else if (state.currWords.isNotEmpty()) {
            WordResultContainer(words = state.currWords) { selectedWord ->
                viewModel.onSelectedWord(word = selectedWord.word)
            }
        }
    }
}