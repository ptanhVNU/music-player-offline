package com.dev.musicplayer.navigation

sealed class Screen(val route: String) {
    object SongsScreen : Screen("songs_screen")

    object PlaylistScreen : Screen("playlist_screen")

    object ImportScreen : Screen("import_screen")

    object PlayerScreen: Screen("player_screen")
}


