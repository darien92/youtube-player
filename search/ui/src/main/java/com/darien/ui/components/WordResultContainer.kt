package com.darien.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darien.data.models.Word

@Composable
fun WordResultContainer(
    modifier: Modifier = Modifier,
    words: List<Word>,
    onItemClicked: (Word) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn {
            items(words) { currWord ->
                WordResultItem(word = currWord) {
                    onItemClicked.invoke(currWord)
                }
            }
        }
    }
}