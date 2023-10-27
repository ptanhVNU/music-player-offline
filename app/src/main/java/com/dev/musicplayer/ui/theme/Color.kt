package com.dev.musicplayer.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver


const val MinContrastOfPrimaryVsSurface = 3f

@Composable
fun ColorScheme.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}

val primaryColor = Color(0xFF1E2129)
val errorColor = Color(0xFFEA6D7E)
val backgroundColor = Color(0xFF121316)

val MusicAppColorScheme = darkColorScheme(
    primary = primaryColor,
    onPrimary = Color.Black,
    background = backgroundColor,
    secondary = primaryColor,
    onSecondary = Color.Black,
    error = errorColor,
    onError = Color.Black
)
