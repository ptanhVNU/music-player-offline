package com.dev.musicplayer.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomSheet(
    playlists: List<Playlist>,
    onDismissRequest: () -> Unit,
    onClicked: (playlist: Playlist) -> Unit,
    bottomSheetState: SheetState,
) {
    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = MusicAppColorScheme.surface,
        onDismissRequest = {
            onDismissRequest()
        },
    ) {
        // Content of the bottom sheet
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = "Thêm nhạc vào danh sách phát",
                textAlign = TextAlign.Center,
                style = MusicAppTypography.titleMedium.copy(
                    color = MusicAppColorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Display playlists using LazyColumn
            LazyColumn {

                items(playlists) { playlist ->
                    PlaylistItem(
                        playlist = playlist,
                        onClicked = { onClicked(playlist) },
                    )
                }
            }
        }
    }

}

@Composable
fun PlaylistItem(playlist: Playlist, onClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClicked()
            }
            .height(75.dp)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp),
            model = "https://i1.sndcdn.com/artworks-y4ek09OJcvON38Ys-gs2icQ-t500x500.jpg",
            contentDescription = "Title Album"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = playlist.title,
            style = MusicAppTypography.titleLarge.copy(
                color = MusicAppColorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        )
    }
}