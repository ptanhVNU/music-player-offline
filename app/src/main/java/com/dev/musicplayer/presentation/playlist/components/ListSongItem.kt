package com.dev.musicplayer.presentation.playlist.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.ui.theme.MusicAppTypography

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