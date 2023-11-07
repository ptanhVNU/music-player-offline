package com.dev.musicplayer.presentation.playlist
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.dev.musicplayer.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Album(
    val ID: Int,
    val imageResource: Int,
    val albumName: String,
    val artistName: String,
    val songCount: Int
)

class AlbumViewModel : ViewModel() {
    private val _albumState = MutableStateFlow(emptyList<Album>())
    val albumState: StateFlow<List<Album>> = _albumState.asStateFlow()

    init {
        _albumState.update {
            sampleAlbum()
        }
    }

    fun refresh() {
        _albumState.update {
            sampleAlbum()
        }
    }

    fun removeItem(currentItem: Album) {
        _albumState.update {
            val mutableList = it.toMutableList()
            mutableList.remove(currentItem)
            mutableList
        }
    }

    private fun sampleAlbum(): List<Album> {
        val albums = listOf(
            Album(ID = 1, imageResource = R.drawable.meme, albumName = "Album 1", artistName = "Artist", songCount = 10),
            Album(ID = 2,imageResource = R.drawable.meme, albumName = "Album 2", artistName = "Artist", songCount = 15),
            Album(ID = 3,imageResource = R.drawable.meme, albumName = "Album 3", artistName = "Artist", songCount = 20),
            Album(ID = 4,imageResource = R.drawable.meme, albumName = "Album 4", artistName = "Artist", songCount = 25),
            Album(ID = 5,imageResource = R.drawable.meme, albumName = "Album 5", artistName = "Artist", songCount = 30),
            Album(ID = 6,imageResource = R.drawable.meme, albumName = "Album 6", artistName = "Artist", songCount = 30),
            Album(ID = 7,imageResource = R.drawable.meme, albumName = "Album 7", artistName = "Artist", songCount = 30),
            Album(ID = 8,imageResource = R.drawable.meme, albumName = "Album 8", artistName = "Artist", songCount = 30),
            Album(ID = 9,imageResource = R.drawable.meme, albumName = "Album 9", artistName = "Artist", songCount = 30),
            Album(ID = 10,imageResource = R.drawable.meme, albumName = "Album 10", artistName = "Artist", songCount = 30)
        )
        return albums
    }
}
@Composable
fun albumScreen(album: Album, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp),
                painter = painterResource(id = album.imageResource),
                contentDescription = "Meme"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = album.albumName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Row {
                    Text(
                        text = album.artistName,
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = " | ",
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = album.songCount.toString() + " songs",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Color(0xFFFF1744)
        DismissDirection.EndToStart -> Color(0xFFFF1744)
        null -> Color.Transparent
    }
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.StartToEnd) Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun albumItem(
    album: Album,
    onRemove: (Album) -> Unit
) {
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(album)
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart
                || it == DismissValue.DismissedToEnd) {
                show = false
                true
            } else false
        }, positionalThreshold = { 150.dp.toPx() }
    )
    AnimatedVisibility(
        show,exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                albumScreen(album, onClick = {})
            }
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800)
            onRemove(currentItem)
            Toast.makeText(context, "Album removed", Toast.LENGTH_SHORT).show()
        }
    }
}