package com.dev.musicplayer.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dev.musicplayer.data.local.entities.Playlist

@Composable
@Preview(showBackground = true)
fun CustomDialogPreview() {
    CustomDialog(
        showDialog = true, playlists = listOf(
            Playlist(
                title = "HELLO",
                createdAt = 0,
            ),
            Playlist(
                title = "asfdadsfds",
                createdAt = 0,
            )
        )
    ) {
    }
}


@Composable
fun CustomDialog(
    showDialog: Boolean,
    playlists: List<Playlist>,
    onDismissRequest: () -> Unit,
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                LazyColumn() {
                    items(
                        playlists
                    ) { playlist ->
                        PlaylistItem(playlist = playlist) { }

                    }
                }


            }
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, onPlaylistSelected: (Playlist) -> Unit) {
    ListItem(
        headlineContent = { Text(playlist.title) },
        modifier = Modifier
            .padding(vertical = 5.dp)
            .clickable { onPlaylistSelected(playlist) }
    )
}