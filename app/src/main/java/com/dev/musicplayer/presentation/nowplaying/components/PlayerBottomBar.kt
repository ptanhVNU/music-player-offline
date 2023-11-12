//package com.dev.musicplayer.presentation.nowplaying
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//
//@Composable
//fun NowPlayingInfo(nowPlayingSong: String, artist: String) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = nowPlayingSong,
//            style = MaterialTheme.typography.bodySmall
//        )
//        Text(
//            text = artist,
//            style = MaterialTheme.typography.bodyLarge
//        )
//    }
//}
//
//@Composable
//fun NowPlayingBottomBar() {
//    NowPlayingInfo(nowPlayingSong = "Đá tan", artist = "Ngọt");
//}