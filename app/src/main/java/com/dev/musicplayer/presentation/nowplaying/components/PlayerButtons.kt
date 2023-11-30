package com.dev.musicplayer.presentation.nowplaying.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.RepeatOneOn
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.ShuffleOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.utils.PlayerState

@Composable
fun PlayerButtons(
    modifier: Modifier = Modifier,
    onShuffleClicked: () -> Unit,
    onSkipPreviousClicked: () -> Unit,
    onPlayPauseClicked: () -> Unit,
    onSkipNextClicked: () -> Unit,
    onRepeatClicked: () -> Unit,
    playerState: PlayerState?,
    isRepeatOneEnabled: Boolean,
    isShuffleEnabled: Boolean,
    playerButtonSize: Dp = 72.dp,
    sideButtonSize: Dp = 42.dp,
    smallSideButtonSize: Dp = 32.dp,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        val buttonModifier = Modifier
            .semantics {
                role = Role.Button
            }

        Image(
            imageVector = if (isShuffleEnabled) {
                Icons.Rounded.ShuffleOn
            } else {
                Icons.Rounded.Shuffle
            },
            contentDescription = "Shuffle",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MusicAppColorScheme.onBackground),
            modifier = buttonModifier
                .size(smallSideButtonSize)
                .offset(y = (5).dp)
                .clickable {
                    onShuffleClicked()
                },
        )

        Spacer(modifier = Modifier.size(30.dp))
        Image(
            imageVector = Icons.Filled.SkipPrevious,
            contentDescription = "Skip Previous",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MusicAppColorScheme.onBackground),
            modifier = buttonModifier
                .size(sideButtonSize)
                .clickable {
                    onSkipPreviousClicked()
                },
        )
        Spacer(modifier = Modifier.size(30.dp))
        Image(
            imageVector = if (playerState == PlayerState.PLAYING) {
                Icons.Rounded.PauseCircle
            } else {
                Icons.Rounded.PlayCircle
            },
            contentDescription = "Play or Pause Button",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MusicAppColorScheme.onBackground),
            modifier = Modifier
                .size(playerButtonSize)
                .offset(y = (-10).dp)
                .semantics { role = Role.Button }
                .clickable {
                    onPlayPauseClicked()
                }
        )
        Spacer(modifier = Modifier.size(30.dp))
        Image(
            imageVector = Icons.Filled.SkipNext,
            contentDescription = "Skip Next",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MusicAppColorScheme.onBackground),
            modifier = buttonModifier
                .size(sideButtonSize)
                .clickable {
                    onSkipNextClicked()
                },
        )
        Spacer(modifier = Modifier.size(30.dp))
        Image(
            imageVector = if (isRepeatOneEnabled) {
                Icons.Rounded.RepeatOneOn
            } else {
                Icons.Rounded.RepeatOne
            },
            contentDescription = "Repeat",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier
                .size(smallSideButtonSize)
                .offset(y = (5).dp)
                .clickable {
                    onRepeatClicked()
                },
        )
    }
}