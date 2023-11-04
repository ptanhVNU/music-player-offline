package com.dev.musicplayer.presentation.home

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
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.ui.theme.MusicAppTypography

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    item: Song,
    onItemClicked: (uri: String) -> Unit,
) {
    Row(

        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item.uri) }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
//        ExploreImageContainer(modifier = Modifier.fillMaxWidth()) {
//            ExploreImage(item)
//        }
        Icon(
            imageVector = Icons.Rounded.Image,

            contentDescription = "Thumbnail",

            modifier = Modifier.size(50.dp),
        )
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

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "MORE",
                tint = Color.White
            )
        }


    }
}