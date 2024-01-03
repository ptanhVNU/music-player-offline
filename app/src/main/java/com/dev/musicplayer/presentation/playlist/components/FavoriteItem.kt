package com.dev.musicplayer.presentation.playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dev.musicplayer.data.local.entities.Playlist

@Composable
fun FavoriteItem(
    onClick: () -> Unit,
    numOfSongs: Int? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(Color.Transparent)
            .padding(10.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            modifier = Modifier.size(40.dp),
            contentDescription = "Favorite Album"
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = "Nhạc đã thích",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (numOfSongs != null)
                Text(
                    text = "$numOfSongs bài",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                )
        }
    }
}