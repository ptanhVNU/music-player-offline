package com.dev.musicplayer.presentation.playlist.listSongOfAlbum

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.presentation.playlist.AlbumViewModel
import com.dev.musicplayer.presentation.playlist.ReturnButton
import com.dev.musicplayer.presentation.playlist.SettingButton
import com.dev.musicplayer.presentation.playlist.StartButton
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun ListSongScreen(
    navController: NavController,
    albumID:Long,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    viewModel.getPlaylistById(albumID)
    val album: Playlist? by viewModel.album.collectAsState()
    Box(
        modifier = Modifier
            .background(MusicAppColorScheme.background)
            .fillMaxSize()
    ) {
        if(album != null) {
            rvBackGroundAlbum(navController, album!!, context)
        }
    }
}


suspend fun loadBitmapFromUrl(url: String, context: Context): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun rvBackGroundAlbum(navController: NavController, album: Playlist, context: Context) {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    var activeStart by remember {
        mutableStateOf(false)
    }
    val url = "https://i1.sndcdn.com/artworks-y4ek09OJcvON38Ys-gs2icQ-t500x500.jpg" ?: ""
//    val url = album.thumbnail ?: ""
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(url) {
        val job = GlobalScope.launch(Dispatchers.IO) {
            val loadedBitmap = loadBitmapFromUrl(url, context)
            withContext(Dispatchers.Main) {
                bitmap.value = loadedBitmap
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Box() {
        bitmap.value?.let { loadedBitmap ->
            val palette = Palette.from(loadedBitmap).generate()
            val swatch = palette.vibrantSwatch

            swatch?.let {
                val backgroundColor = it.rgb
                val alpha = 0.8f
                Box(
                    modifier = Modifier
                        .background(Color(backgroundColor).copy(alpha = alpha))
                        .height(450.dp)
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(10.dp))
                            ReturnButton(
                                icon = Icons.Default.ArrowBackIos,
                                onClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(200.dp)
                                    .align(Alignment.CenterHorizontally),
//                                model = album.thumbnail,
                                model = "https://i1.sndcdn.com/artworks-y4ek09OJcvON38Ys-gs2icQ-t500x500.jpg",
                                contentDescription = "Title Album"
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(30.dp))
                                Text(
                                    text = album.title,
                                    color = Color.White,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(30.dp))
                                Text(
                                    text = "Danh sách phát của bạn",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row {
                                    Spacer(modifier = Modifier.width(30.dp))
                                    StartButton(
                                        icon = Icons.Default.PlayArrow,
                                        onClick = {
                                            activeStart = true
                                        }
                                    )
                                }
                                SettingButton(
                                    icon = Icons.Default.MoreVert,
                                    onClick = {
                                        showBottomSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
