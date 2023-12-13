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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.presentation.playlist.components.DisplayBackgroundAlbum
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
    viewModel: PlaylistViewModel,
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


//    val musicItems = remember {
//        mutableStateListOf<MusicItem>().apply {
//            addAll(musics.map { MusicItem(it) })
//        }
//    }


    var selectedMusicIndex by remember { mutableIntStateOf(-1) }

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
                        onClick = onPlayMusicButtonClicked,
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
                    val isSelected = selectedMusicIndex == songsInPlaylist.indexOf(music)

                    SongItem(
                        item = music,
                        musicPlaybackUiState = musicPlaybackUiState,
                        onItemClicked = {
                            viewModel.addMusicItems(songsInPlaylist)

                            if (!isSelected) {
                                selectedMusicIndex = songsInPlaylist.indexOf(music)
                            }

                            onEvent(MusicEvent.OnMusicSelected(music))
                            onEvent(MusicEvent.PlayMusic)
                        },
                        isInPlaylist = true,
                        isSelected = isSelected,
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


            /// convert to modal bottom sheet
//                if (showDialog) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.Transparent)
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .height(500.dp)
//                                .width(400.dp)
//                                .background(Color.Black),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Spacer(modifier = Modifier.height(20.dp))
//                            LazyColumn(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(bottom = 80.dp),
//                                //                            verticalArrangement = Arrangement.Center,
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                state = scrollState,
//                            ) {
//                                items(musicItems.size) { i ->
//                                    Row(
//                                        modifier = Modifier
//                                            .clickable {
//                                                musicItems[i] =
//                                                    musicItems[i].copy(isClicked = !musicItems[i].isClicked)
//                                            }
//                                    ) {
//                                        ListSongItem(item = musicItems[i].item)
//                                        Spacer(modifier = Modifier.width(30.dp))
//                                        Column {
//                                            Spacer(modifier = Modifier.height(5.dp))
//                                            if (musicItems[i].isClicked) {
//                                                Icon(
//                                                    imageVector = Icons.Default.Check,
//                                                    contentDescription = null,
//                                                    tint = Color.White
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            Button(
//                                onClick = {
//                                    showDialog = false
//                                    val clickedItems = musicItems.filter { it.isClicked }
//                                    clickedItems.map {
//                                        viewModel.addSongToPlaylist(albumID, it.item)
//                                    }
//                                    Log.d("Test", "{${playlist?.songs}")
//                                    musicItems.forEach { it.isClicked = false }
//                                },
//                                modifier = Modifier.align(Alignment.BottomCenter)
//                            ) {
//                                Text("Submit")
//                            }
//                        }
//                    }
//                }
        }
    }
}




