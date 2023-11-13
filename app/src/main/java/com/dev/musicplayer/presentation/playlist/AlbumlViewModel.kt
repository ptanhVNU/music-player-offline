package com.dev.musicplayer.presentation.playlist
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.height
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.dev.musicplayer.R
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlbumViewModel : ViewModel() {
    private val _albumState = MutableStateFlow(emptyList<Playlist>())
    val albumState: StateFlow<List<Playlist>> = _albumState.asStateFlow()

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

    fun removeItem(currentItem: Playlist) {
        _albumState.update {
            val mutableList = it.toMutableList()
            mutableList.remove(currentItem)
            mutableList
        }
    }

    private fun sampleAlbum() = listOf(
            Playlist(id = 1, thumbnail = "https://m.media-amazon.com/images/I/51XQrcwyhkL._UF1000,1000_QL80_.jpg", title = "Album 1", songs = listOf("Song1", "Song2", "Song3"), createdAt = 1),
            Playlist(id = 2, thumbnail = "https://pics.craiyon.com/2023-05-30/ca4e47993f67455a98ec3b939df5c08c.webp", title = "Album 2", songs = listOf("Song1", "Song2", "Song3"), createdAt = 2),
            Playlist(id = 3, thumbnail = "https://play-lh.googleusercontent.com/qEpilTvPX27gd0qvv9g2oyzCpYIIJSEtHMDbEXfvvItPMnudkeN01TzD5GO3iOCHByp9", title = "Album 3", songs = listOf("Song1", "Song2", "Song3"), createdAt = 3),
            Playlist(id = 4, thumbnail = "https://pics.craiyon.com/2023-05-30/ca4e47993f67455a98ec3b939df5c08c.webp", title = "Album 4", songs = listOf("Song1", "Song2", "Song3"), createdAt = 4),
            Playlist(id = 5, thumbnail = "https://m.media-amazon.com/images/I/51XQrcwyhkL._UF1000,1000_QL80_.jpg", title = "Album 5", songs = listOf("Song1", "Song2", "Song3"), createdAt = 5),
            Playlist(id = 6, thumbnail = "https://pics.craiyon.com/2023-05-30/ca4e47993f67455a98ec3b939df5c08c.webp", title = "Album 6", songs = listOf("Song1", "Song2", "Song3"), createdAt = 6),
            Playlist(id = 7, thumbnail = "https://play-lh.googleusercontent.com/qEpilTvPX27gd0qvv9g2oyzCpYIIJSEtHMDbEXfvvItPMnudkeN01TzD5GO3iOCHByp9", title = "Album 7", songs = listOf("Song1", "Song2", "Song3"), createdAt = 7),
            Playlist(id = 8, thumbnail = "https://m.media-amazon.com/images/I/51XQrcwyhkL._UF1000,1000_QL80_.jpg", title = "Album 8", songs = listOf("Song1", "Song2", "Song3"), createdAt = 8),
            Playlist(id = 9, thumbnail = "https://pics.craiyon.com/2023-05-30/ca4e47993f67455a98ec3b939df5c08c.webp", title = "Album 9", songs = listOf("Song1", "Song2", "Song3"), createdAt = 9),
            Playlist(id = 10, thumbnail = "https://play-lh.googleusercontent.com/qEpilTvPX27gd0qvv9g2oyzCpYIIJSEtHMDbEXfvvItPMnudkeN01TzD5GO3iOCHByp9", title = "Album 10", songs = listOf("Song1", "Song2", "Song3"), createdAt = 10),
    )

    fun getAlbumById(albumId: Long): Playlist? {
        return sampleAlbum().find { it.id == albumId }
    }
}

@Composable
fun albumScreen(album: Playlist, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .clickable(onClick =  onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp),
                model = album.thumbnail,
                contentDescription = "Title Album"
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = album.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${album.songs?.size ?: 0} songs",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                )
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
    album: Playlist,
    onRemove: (Playlist) -> Unit,
    navController: NavController
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
                albumScreen(album, onClick = { navController.navigate("listSong/${album.id}") })
            }
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(1000)
            onRemove(currentItem)
            Toast.makeText(context, "Album removed", Toast.LENGTH_SHORT).show()
        }
    }
}