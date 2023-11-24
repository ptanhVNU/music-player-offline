package com.dev.musicplayer.presentation.nowplaying


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.R
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.presentation.nowplaying.components.PlayerButtons
import com.dev.musicplayer.presentation.nowplaying.components.PlayerSlider
import com.dev.musicplayer.presentation.nowplaying.components.SongDescription
import com.dev.musicplayer.presentation.nowplaying.components.TopPlayerScreenBar
import com.dev.musicplayer.utils.PlayerState

@Composable
fun PlayerScreen(
    onEvent: (MusicPlayerEvent) -> Unit,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(horizontal = 10.dp)
    ) {
        TopPlayerScreenBar {
            onNavigateUp()
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(
                    id = R.drawable.test_player
                ),
                contentDescription = "Song Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .weight(10f)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(10f)
            ) {
                with(musicPlaybackUiState) {
                    currentMusic?.run {
                        SongDescription(title = title, name = artist)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    PlayerSlider(
                        currentPosition = currentPosition,
                        totalDuration = totalDuration,
                        onValueChanged = {
                            onEvent(MusicPlayerEvent.SeekMusicPosition(it.toLong()))
                        }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    PlayerButtons(
                        modifier = Modifier.padding(vertical = 8.dp),
                        playerState = playerState,
                        onPlayPauseClicked = {
                            when (playerState) {
                                PlayerState.PLAYING -> onEvent(MusicPlayerEvent.PauseMusic)
                                PlayerState.PAUSED -> onEvent(MusicPlayerEvent.ResumeMusic)
                                else -> {}
                            }
                        },
                        onSkipPreviousClicked = {
                            onEvent(MusicPlayerEvent.SkipPreviousMusic)
                        },
                        onSkipNextClicked = {
                            onEvent(MusicPlayerEvent.SkipNextMusic)
                        },
                        onShuffleClicked = {
                            onEvent(MusicPlayerEvent.SkipPreviousMusic)
                        },
                        onRepeatClicked = {
                            if (isRepeatOneEnabled) {
                                onEvent(MusicPlayerEvent.SetPlayerRepeatOneEnabled(false))
                            } else {
                                onEvent(MusicPlayerEvent.SetPlayerRepeatOneEnabled(true))
                            }
                        },
                        isRepeatOneEnabled = isRepeatOneEnabled,
                        isShuffleEnabled = isShuffleEnabled,
                    )
                }

            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}