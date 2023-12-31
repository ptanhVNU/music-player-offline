package com.dev.musicplayer.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dev.musicplayer.R
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.utils.WaveAnimation
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    isInPlaylist: Boolean = false,
    item: MusicEntity,
    musicPlaybackUiState: MusicPlaybackUiState,
    onItemClicked: () -> Unit,
    onAddToPlaylist: (() -> Unit)? = null,
    onDeleteClicked: (() -> Unit)? = null,
) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked()
            }
            .height(75.dp)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        with(musicPlaybackUiState) {
            if (isSelected) {
                if (playerState == PlayerState.PLAYING) {
                    WaveAnimation(true)
                } else if (playerState == PlayerState.PAUSED) {
                    WaveAnimation(false)
                }
            } else {
                AsyncImage(
                    modifier = Modifier
                        .size(50.dp)
                        .shadow(
                            elevation = 1.dp,
                            shape = MaterialTheme.shapes.small
                        )
                        .clip(MaterialTheme.shapes.small),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.icon_music)
                        .build(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Music cover"
                )
            }
        }

        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
        ) {
            Text(
                text = item.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MusicAppTypography.bodyLarge
                    .copy(
                        color = if (isSelected) MusicAppColorScheme.tertiary else MusicAppColorScheme.onBackground,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.W400
                    ),
            )

            Text(
                text = if (item.artist.equals("<unknown>")) "Unknown" else item.artist,
                style = MusicAppTypography.titleSmall.copy(
                    color = Color.Gray
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        DropDownMenuButton(
            onAddPlayList = onAddToPlaylist ?: {},
            isInPlaylist = isInPlaylist,
            onDeleteClicked = onDeleteClicked,
        )


    }
}



