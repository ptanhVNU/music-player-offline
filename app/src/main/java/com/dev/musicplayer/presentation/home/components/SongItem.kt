package com.dev.musicplayer.presentation.home.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.ui.theme.MusicAppTypography

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    item: MediaAudioItem,
    onItemClicked: () -> Unit,
//    onDeleteSong: (Song) -> Unit,

) {
    Row(

        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked()
            }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        if (item.artWork != null)
            AsyncImage(
                model = item.artWork,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit,
            ) else
            Box(modifier = Modifier.size(50.dp)) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center),
                    contentDescription = "Music",
                )
            }
        Spacer(
            modifier = Modifier
                .height(24.dp)
                .width(8.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.SpaceAround,

            ) {
            Text(
//                modifier = Modifier.fillMaxWidth(),
                text = item.name,
                style = MusicAppTypography.titleMedium,
            )
            Text(
//                modifier = Modifier.fillMaxWidth(),
                text = item.artist,
                style = MusicAppTypography.titleSmall.copy(
                    color = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

//        DropDownMenuButton(
//            onAddPlayList = {},
//            onDeleteSong = {
//            }
//        )

    }
}