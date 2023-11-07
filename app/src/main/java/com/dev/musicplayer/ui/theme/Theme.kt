package com.dev.musicplayer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MusicAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MusicAppColorScheme,
        typography = MusicAppTypography,
        shapes = MusicAppShapes,
        content = content
    )
}
