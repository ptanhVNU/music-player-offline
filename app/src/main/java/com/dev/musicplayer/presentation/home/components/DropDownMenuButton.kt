package com.dev.musicplayer.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dev.musicplayer.ui.theme.MusicAppTypography

@Composable
fun DropDownMenuButton(
    onAddPlayList: () -> Unit,
    onDeleteClicked: (() -> Unit?)? = null,
    isInPlaylist: Boolean,
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        if (!isInPlaylist) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.PlaylistAdd,
                            contentDescription = "Add to playlist",
                        )
                    },
                    text = { Text("Add to Playlist", style = MusicAppTypography.bodySmall) },
                    onClick = {
                        expanded = false
                        onAddPlayList()
                    }
                )
            }
        } else {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete from playlist",
                        )
                    },
                    text = { Text("Delete from playlist", style = MusicAppTypography.bodySmall) },
                    onClick = {
                        expanded = false
                        if (onDeleteClicked != null) {
                            onDeleteClicked()
                        }
                    }
                )
            }
        }
    }
}