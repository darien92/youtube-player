package com.darien.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darien.data.models.Word

@Composable
fun WordResultItem(
    modifier: Modifier = Modifier,
    word: Word,
    onWordSelected: (String) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(60.dp)
        .clickable {
            onWordSelected.invoke(word.word)
        }) {
        Column(modifier.fillMaxWidth()) {
            Text(text = word.word)
            Divider()
        }
    }
}