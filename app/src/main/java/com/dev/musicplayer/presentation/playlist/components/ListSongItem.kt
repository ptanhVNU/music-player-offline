package com.dev.musicplayer.presentation.playlist.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dev.musicplayer.R
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.home.components.DropDownMenuButton
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.presentation.playlist.AlbumViewModel
import com.dev.musicplayer.presentation.playlist.listSongOfAlbum.ListSongViewModel
import com.dev.musicplayer.presentation.utils.WaveAnimation
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState

@Composable
fun ListSongItem(
    modifier: Modifier = Modifier,
    item: MusicEntity
) {
    Row(
        modifier = modifier
            .width(300.dp)
            .padding(5.dp),

        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),

            verticalArrangement = Arrangement.SpaceBetween,

            ) {
            Row{
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = item.title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MusicAppTypography.bodyLarge,
                    )
                    Text(
                        text = if (item.artist .equals("<unknown>") ) "Unknown" else item.artist,
                        style = MusicAppTypography.titleSmall.copy(
                            color = Color.Gray
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSongItemView(
    item: MusicEntity,
    listSongViewModel: ListSongViewModel,
    musicPlaybackUiState : MusicPlaybackUiState,
    onItemClicked: () -> Unit,
    isInPlaylist: Boolean,
    isSelected: Boolean
) {
    var show by remember { mutableStateOf(true) }
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if ((it == DismissValue.DismissedToStart ||
                        it == DismissValue.DismissedToEnd)
            ) {
                listSongViewModel.deleteSong(item)
                show = false
                true
            } else false
        }
    )
    AnimatedVisibility(
        visible = show,
        exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                SongItem(
                    item = item,
                    musicPlaybackUiState = musicPlaybackUiState,
                    onItemClicked = onItemClicked,
                    isInPlaylist = isInPlaylist,
                    isSelected = isSelected,
                )
            }
        )
    }
}