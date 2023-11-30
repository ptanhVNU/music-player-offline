package com.dev.musicplayer.presentation.home.components

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.ui.theme.MusicAppTypography

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    item: Song,
    onItemClicked: () -> Unit,
    onDeleteSong: (Song) -> Unit,
    onEditSong: (Song) -> Unit,
) {
    Row(

        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Log.d("TAG", "SongItem: ${item.thumbnail}")

// Sử dụng hàm loadResource để tải tệp và tạo ImageBitmap

        AsyncImage(
//            model = item.uri,
            model = "https://i1.sndcdn.com/artworks-y4ek09OJcvON38Ys-gs2icQ-t500x500.jpg",
            contentDescription = "",
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Crop
        )

//        ExploreImageContainer(modifier = Modifier.fillMaxWidth()) {
//            ExploreImage(item)
//        }


        Spacer(
            modifier = Modifier
                .height(24.dp)
                .width(8.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
//                modifier = Modifier.fillMaxWidth(),
                text = item.title,
                style = MusicAppTypography.titleMedium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
//                modifier = Modifier.fillMaxWidth(),
                text = item.artistName, style = MusicAppTypography.titleSmall.copy(
                    color = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        DropDownMenuButton(
            onAddPlayList = {},
            onDeleteSong = {
                onDeleteSong(item)
            },
            onEditSong = {
                onEditSong(item)
            }
        )

    }
}