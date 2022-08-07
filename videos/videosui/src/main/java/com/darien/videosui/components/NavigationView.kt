package com.darien.videosui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationView(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {

        IconButton(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .padding(start = 8.dp),
            onClick = {
                onBackPressed.invoke()
            }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Search"
            )
        }
    }
}