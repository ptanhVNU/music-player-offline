package com.dev.musicplayer.presentation.playlist.listSongOfAlbum

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.home.components.SongItem
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
@OptIn(ExperimentalMaterial3Api::class)
fun ListSongScreen(
    navController: NavController,
    albumID:Long,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    viewModel.getPlaylistById(albumID)
    val album: Playlist? by viewModel.album.collectAsState()

    val listOfSongs = remember { mutableStateOf<List<Song>>(emptyList()) }

    LaunchedEffect(album?.songs.hashCode()) {
        val songs = album?.songs?.map { string ->
            viewModel.toFormattedSong(string)
        } ?: emptyList()

        listOfSongs.value = songs
    }

    var showSettingSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val selectAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) {
        viewModel.selectMusicFromStorage(albumID, it)

        if (it.size == 1)
            Toast.makeText(context, "Added ${it.size} song success", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, "Added ${it.size} songs success", Toast.LENGTH_LONG).show()
    }

    Box(
        modifier = Modifier
            .background(MusicAppColorScheme.background)
            .fillMaxSize()
    ) {
        val scrollState = rememberLazyListState()
        if(album != null) {
            Column {
                rvBackGroundAlbum(navController, album!!, context, showSettingSheet) {
                    showSettingSheet = it
                }
                Box(
                ) {
                    Log.d("Danh sách nhạc", "{${album?.songs}}")
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        state = scrollState,
                    ) {
                        itemsIndexed(
                            items = album?.songs?.map { string ->
                                viewModel.toFormattedSong(string)
                            } ?: emptyList(),
                            key = { _, item -> item.hashCode() }
                        ) { _, item ->
//                            SongItem(
//                                item = item,
//                                musicPlaybackUiState = musicPlaybackUiState,
//                                onItemClicked = {
//                                    onEvent(MusicEvent.OnMusicSelected(it))
//                                    onEvent(MusicEvent.PlayMusic)
//                                }
//                            )
                        }
                    }
                }
            }
            if (showSettingSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showSettingSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    selectAudioLauncher.launch(
                                        "audio/*"
                                    )
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showSettingSheet = false
                                        }
                                    }
                                }
                        ) {
                            Text("+ Thêm nhạc vào album")
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier
                                .clickable {
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showSettingSheet = false
                                        }
                                    }
                                }
                        ) {
                            Text("+ Chỉnh sửa tiêu đề của album")
                        }
                    }
                }
            }
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
fun rvBackGroundAlbum(
    navController: NavController,
    album: Playlist,
    context: Context,
    showSettingSheet: Boolean,
    setShowSettingSheet: (Boolean) -> Unit) {
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
                        .height(400.dp)
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
                                    .size(150.dp)
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
                                        setShowSettingSheet(true)
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
