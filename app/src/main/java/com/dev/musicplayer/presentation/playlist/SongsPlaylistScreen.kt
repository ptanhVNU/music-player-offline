package com.dev.musicplayer.presentation.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.presentation.playlist.components.DisplayBackgroundAlbum
import com.dev.musicplayer.presentation.search.SearchViewModel
import com.dev.musicplayer.presentation.utils.MusicMiniPlayerCard
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState
import kotlinx.coroutines.launch


data class MusicItem(
    val item: MusicEntity,
    var isClicked: Boolean = false
)


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SongsPlaylistScreen(
    onBackButtonClicked: () -> Unit,
    onPlayMusicButtonClicked: () -> Unit,
    playlist: Playlist,
    allSongs: List<MusicEntity>,
    songsInPlaylist: List<MusicEntity>,
    playlistViewModel: PlaylistViewModel,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    onEvent: (MusicEvent) -> Unit,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateToMusicPlayer: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    var showSettingSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val musicItems = remember {
        mutableStateListOf<MusicItem>().apply {
            addAll(allSongs.map { MusicItem(it) })
        }
    }

    var selectedSongs by remember { mutableStateOf<MutableSet<MusicEntity>>(mutableSetOf()) }

    val selectedSong by playlistViewModel.selectedSong.collectAsState()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .background(MusicAppColorScheme.background)
                .fillMaxSize()
        ) {
            val scrollState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = scrollState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                   DisplayBackgroundAlbum(
                       onBackButtonClicked = onBackButtonClicked,
                       playlist = playlist,
                       setShowSettingSheet = {
                           showSettingSheet = it
                       }
                   )

                    FilledTonalButton(
                        onClick = onPlayMusicButtonClicked ,
                        enabled = songsInPlaylist.isNotEmpty(),
                    ) {
                        Text(
                            "PHÁT NHẠC",
                            style = MusicAppTypography.titleMedium.copy(
                                color = MusicAppColorScheme.onPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500
                            )
                        )
                    }
                }

                items(songsInPlaylist) { music ->
                    SongItem(
                        item = music,
                        musicPlaybackUiState = musicPlaybackUiState,
                        onItemClicked = {
                            playlistViewModel.addMusicItems(songsInPlaylist)

                            onEvent(MusicEvent.OnMusicSelected(music))
                            onEvent(MusicEvent.PlayMusic)

                            playlistViewModel.setSelectedSong(music)
                            homeViewModel.setSelectedSong(null)
                            searchViewModel.setSelectedSong(null)
                        },
                        isInPlaylist = true,
                        isSelected = music == selectedSong,
                        onDeleteClicked = {
                            playlistViewModel.deleteSongFromPlaylist(music.id.toLong(), playlist.id)
                        }
                    )
                }

                item {
                    Text(
                        modifier = Modifier
                            .height(180.dp)
                            .padding(5.dp)
                            .align(Alignment.CenterStart),
                        text = "Tổng số bài hát: ${songsInPlaylist.size}",
                        textAlign = TextAlign.Center,
                        style = MusicAppTypography.headlineMedium,
                    )
                }

            }

            /// DONE
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
                        musicPlaybackUiState = musicPlaybackUiState
                    )
                }
            }


            /// config
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
                            .padding(top = 0.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                    ) {
                        SettingOptionItem(text = "Thêm nhạc vào album", onClicked = {
                            showDialog = true
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showSettingSheet = false
                                }
                            }
                        })
                    }
                }
            }




            /// convert to modal bottom sheet
//                if (showDialog) {
//                  MusicListScreen(musicList = allSongs, selectedSongs = selectedSongs) {
//
//                  }
//                    Box(
//                        modifier = Modifier
//                            .height(500.dp)
//                            .width(400.dp)
//                            .background(MusicAppColorScheme.surface),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        val scrollState1 = rememberLazyListState()
//                        Spacer(modifier = Modifier.height(20.dp))
//                        LazyColumn(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(bottom = 80.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            state = scrollState1,
//                        ) {
//                            items(musicItems.size) { i ->
//                                Row(
//                                    modifier = Modifier
//                                        .clickable {
//                                            musicItems[i] =
//                                                musicItems[i].copy(isClicked = !musicItems[i].isClicked)
//                                        }
//                                ) {
//                                    ListSongItem(item = musicItems[i].item)
//                                    Spacer(modifier = Modifier.width(30.dp))
//                                    Column {
//                                        Spacer(modifier = Modifier.height(5.dp))
//                                        if (musicItems[i].isClicked) {
//                                            Icon(
//                                                imageVector = Icons.Default.Check,
//                                                contentDescription = null,
//                                                tint = Color.White
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        Button(
//                            onClick = {
//                                showDialog = false
//                                val clickedItems = musicItems.filter { it.isClicked }
//                                clickedItems.forEach {
//                                    viewModel.addSongToPlaylist(playlist.id, it.item)
//                                }
//                                Log.d("Test", "{${playlist?.songs}")
//                                musicItems.forEach { it.isClicked = false }
//                            },
//                            modifier = Modifier.align(Alignment.BottomCenter)
//                        ) {
//                            Text("Submit")
//                        }
//                    }
//                }
        }
    }
}


@Composable
fun SettingOptionItem(text: String, onClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClicked()
            }
            .height(50.dp)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add songs to playlist")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MusicAppTypography.titleLarge.copy(
                color = MusicAppColorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        )
    }
}




