package com.darien.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDims(
    val margin_xs: Dp,
    val margin_md: Dp,
    val result_view_image_size: Dp,
    val result_view_row_size: Dp
)

@Composable
fun appDims() = standardAppDims

private val standardAppDims = AppDims(
    margin_xs = 8.dp,
    margin_md = 16.dp,
    result_view_image_size = 100.dp,
    result_view_row_size = 120.dp
)