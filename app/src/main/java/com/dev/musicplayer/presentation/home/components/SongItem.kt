package com.dev.musicplayer.presentation.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.ui.theme.MusicAppTypography

@SuppressLint("UnrememberedMutableState")
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    item: MusicEntity,
    onItemClicked: () -> Unit,
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
        if (item.image.isNotEmpty())
            AsyncImage(
                modifier = Modifier
                    .size(45.dp)
                    .shadow(
                        elevation = 1.dp,
                        shape = MaterialTheme.shapes.small
                    )
                    .clip(MaterialTheme.shapes.small),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.image)
                    .build(),
                contentScale = ContentScale.FillBounds,
                contentDescription = "Music cover"
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
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),

            verticalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(
//                modifier = Modifier.fillMaxWidth(),
                text = item.title,
                style = MusicAppTypography.bodyLarge,
            )

            Text(
                text = item.artist,
                style = MusicAppTypography.titleSmall.copy(
                    color = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        DropDownMenuButton(
            onAddPlayList = {},
            onDeleteSong = { },
            onEditSong = {},
        )

    }
}