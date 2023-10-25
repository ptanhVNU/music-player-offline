package com.dev.musicplayer.navigation

sealed class Screen(val route: String) {
    object SongsScreen : Screen("songs")

    object PlaylistScreen : Screen("playlist")

    object ImportScreen : Screen("import")

    object PlayerScreen: Screen("player")
}


