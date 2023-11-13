package com.dev.musicplayer.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("songs")

    object PlaylistScreen : Screen("playlist")

    object SettingScreen : Screen("setting")

    object PlayerScreen: Screen("player")

    object ListSongScreen : Screen("listSong")
}


