package com.dev.musicplayer.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.utils.PlayerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicMiniPlayerCard(
    modifier: Modifier = Modifier,
    music: Song?,
    playerState: PlayerState?,
    onResumeClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                music?.run {
                   Icon(imageVector = Icons.Default.MusicNote, contentDescription = null)
//                    AsyncImage(
//                        modifier = Modifier
//                            .size(45.dp)
//                            .clip(MaterialTheme.shapes.small),
//                        model = ImageRequest.Builder(LocalContext.current)
//                            .data(thumbnail)
//                            .build(),
//                        contentScale = ContentScale.FillBounds,
//                        contentDescription = "Music cover"
//                    )
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
                            text = artistName,
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
    }
}