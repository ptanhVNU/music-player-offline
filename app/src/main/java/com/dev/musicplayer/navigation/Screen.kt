package com.dev.musicplayer.navigation

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("songs")

    data object PlaylistScreen : Screen("playlist")

    data object SettingScreen : Screen("setting")

    object ListSongScreen : Screen("listSong")

    data object PlayerScreen: Screen("player")

}


