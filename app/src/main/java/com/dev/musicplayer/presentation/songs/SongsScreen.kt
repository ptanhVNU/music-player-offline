package com.dev.musicplayer.presentation.songs

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen() {
    val context = LocalContext.current

    // Create the ExoPlayer.
    val player = ExoPlayer.Builder(context).build()

    val mp3Path = "assets/audio/audio.mp3"

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Text(
                        text = "Songs",
                        fontWeight = FontWeight.Bold,
                        style = MusicAppTypography.headlineMedium,
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            //TODO: Implement search bar
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search music",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MusicAppColorScheme.background)

            )


        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            Box(modifier = Modifier
                .size(width = 100.dp, height = 50.dp)
                .clickable {
                    val mediaItem = MediaItem.fromUri(Uri.parse(mp3Path))
                    println("media item:${mediaItem.mediaId}")
                    println("test")
                    player.addMediaItem(mediaItem)

                    player.prepare()

                    player.play()
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center),
                    imageVector = Icons.Outlined.PlayCircle,
                    contentDescription = "Play Music",
                )
            }
        }
    }
}