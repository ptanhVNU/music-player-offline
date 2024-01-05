package com.dev.musicplayer.presentation.playlist.components

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.home.HomeUiState
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.presentation.playlist.AlbumViewModel
import com.dev.musicplayer.presentation.playlist.ReturnButton
import com.dev.musicplayer.presentation.playlist.SettingButton
import com.dev.musicplayer.presentation.playlist.StartButton
import com.dev.musicplayer.presentation.playlist.listSongOfAlbum.ListSongUiState
import com.dev.musicplayer.presentation.playlist.listSongOfAlbum.ListSongViewModel
import com.dev.musicplayer.presentation.utils.MusicMiniPlayerCard
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type


data class MusicItem(
    val item: MusicEntity,
    var isClicked: Boolean = false
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ListSongScreen(
    navController: NavController,
    albumID: Long,
    album: Playlist?,
    song: List<MusicEntity>,
    albumViewModel: AlbumViewModel,
    viewModel: ListSongViewModel,
    homeUiState: HomeUiState,
    listSongUiState: ListSongUiState,
    onEvent: (MusicEvent) -> Unit,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateToMusicPlayer: () -> Unit
) {
    val context = LocalContext.current
//    viewModel.getPlaylistById(albumID)
//    val album: Playlist? by viewModel.album.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    var showSettingSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val musics = homeUiState.musics ?: emptyList()

    val musicItems = remember {
        mutableStateListOf<MusicItem>().apply {
            addAll(musics.map { MusicItem(it) })
        }
    }
    val musicEntities: List<MusicEntity>? = album?.songs?.let { songs ->
        songs.map { viewModel.toFormattedMusicEntity(it) }
    }
    val musicListToShow: List<MusicEntity> = musicEntities ?: listOf()

    var selectedMusicIndex by remember { mutableIntStateOf(-1) }

    Scaffold() { innerPadding ->
        Box(
            modifier = Modifier
                .background(MusicAppColorScheme.background)
                .fillMaxSize()
        ) {
            val scrollState = rememberLazyListState()
            with(listSongUiState) {
                when(loading) {
                    true -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    false -> {
                        if (album != null) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                state = scrollState,
                            ) {

                                item {
                                    DisplayBackgroundAlbum(navController, album!!, context) {
                                        showSettingSheet = it
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Button(
                                            onClick = {
                                            }
                                        ) {
                                            Text("Phát nhạc")
                                        }
                                    }
                                }

                                items(song) { music ->
                                    val isSelected = selectedMusicIndex == musics.indexOf(music)
                                    ListSongItemView(
                                        item = music,
                                        listSongViewModel = viewModel,
                                        musicPlaybackUiState = musicPlaybackUiState,
                                        onItemClicked = {
                                            if (!isSelected) {
                                                selectedMusicIndex = musics.indexOf(music)
                                            }
                                            onEvent(MusicEvent.OnMusicSelected(music))
                                            onEvent(MusicEvent.PlayMusic)
                                        },
                                        isInPlaylist = true,
                                        isSelected = isSelected,
                                    )
//                                    SongItem(
//                                        item = music,
//                                        musicPlaybackUiState = musicPlaybackUiState,
//                                        onItemClicked = {
//                                            if (!isSelected) {
//                                                selectedMusicIndex = musics.indexOf(music)
//                                            }
//                                            onEvent(MusicEvent.OnMusicSelected(music))
//                                            onEvent(MusicEvent.PlayMusic)
//                                        },
//                                        isInPlaylist = true,
//                                        isSelected = isSelected,
//                                    )
                                }

                            }

                            with(musicPlaybackUiState) {
                                if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED) {
                                    MusicMiniPlayerCard(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .offset(y = (-5).dp)
                                            .align(Alignment.BottomCenter)
                                            .background(color = MusicAppColorScheme.secondaryContainer)
                                            .clickable { onNavigateToMusicPlayer() },
                                        music = currentMusic,
                                        playerState = playerState,
                                        onResumeClicked = { onEvent(MusicEvent.ResumeMusic) },
                                        onPauseClicked = { onEvent(MusicEvent.PauseMusic) },
                                    )
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
                                                    showDialog = true
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

                            if (showDialog) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Transparent)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(500.dp)
                                            .width(400.dp)
                                            .background(Color.Black),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(bottom = 80.dp),
                                            //                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            state = scrollState,
                                        ) {
                                            items(musicItems.size) { i ->
                                                Row(
                                                    modifier = Modifier
                                                        .clickable {
                                                            musicItems[i] =
                                                                musicItems[i].copy(isClicked = !musicItems[i].isClicked)
                                                        }
                                                ) {
                                                    ListSongItem(item = musicItems[i].item)
                                                    Spacer(modifier = Modifier.width(30.dp))
                                                    Column {
                                                        Spacer(modifier = Modifier.height(5.dp))
                                                        if (musicItems[i].isClicked) {
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = null,
                                                                tint = Color.White
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                        Button(
                                            onClick = {
                                                showDialog = false
                                                val clickedItems = musicItems.filter { it.isClicked }
                                                val selectedSongs = clickedItems.map { it.item }
                                                albumViewModel.addSongsToPlaylist(albumID, selectedSongs)
                                                musicItems.forEach { it.isClicked = false }
                                            },
                                            modifier = Modifier.align(Alignment.BottomCenter)
                                        ) {
                                            Text("Submit")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }
}




