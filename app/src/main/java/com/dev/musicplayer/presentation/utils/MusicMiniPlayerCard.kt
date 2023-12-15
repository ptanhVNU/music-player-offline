package com.dev.musicplayer.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dev.musicplayer.R
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.nowplaying.MusicPlayerEvent
import com.dev.musicplayer.utils.PlayerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicMiniPlayerCard(
    modifier: Modifier,
    music: MusicEntity?,
    playerState: PlayerState?,
    musicPlaybackUiState: MusicPlaybackUiState,
//    onEvent: (MusicPlayerEvent) -> Unit,
    onResumeClicked: () -> Unit,
    onPauseClicked: () -> Unit,

    ) {
    Box(
        modifier = modifier,

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                music?.run {
                    AsyncImage(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(MaterialTheme.shapes.small),
                        model = coil.request.ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.icon_music)
                            .build(),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "Music cover"
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = if (artist.equals("<unknown>")) "Unknown" else artist,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            IconButton(
                onClick = {
                    when (playerState) {
                        PlayerState.PLAYING -> onPauseClicked()
                        PlayerState.PAUSED -> onResumeClicked()
                        else -> {}
                    }
                }
            ) {
                Icon(
                    imageVector = if (playerState == PlayerState.PLAYING) {
                        Icons.Rounded.Pause
                    } else {
                        Icons.Rounded.PlayArrow
                    },
                    contentDescription = "Play or pause button"
                )
            }
        }
//
//        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
//            Slider(
//
//                value = musicPlaybackUiState.currentPosition.toFloat(),
//                valueRange = 0f..musicPlaybackUiState.totalDuration.toFloat(),
//                onValueChange = {
////                    onEvent(MusicPlayerEvent.SeekMusicPosition(it.toLong()))
//                },
//                colors = SliderDefaults.colors(
//                    activeTickColor = Color.Transparent,
//                    inactiveTickColor = Color.Transparent,
//                    inactiveTrackColor = Color.LightGray,
//                    activeTrackColor = Color.Black,
//                    thumbColor = Color.Black
//                )
//            )
//        }
    }
}