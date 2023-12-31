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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.playlist.components.FavoriteItem
import com.dev.musicplayer.presentation.playlist.components.PlaylistItemView
import com.dev.musicplayer.presentation.playlist.components.PlusButton
import com.dev.musicplayer.presentation.playlist.components.SortButton
import com.dev.musicplayer.presentation.utils.MusicMiniPlayerCard
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    playlist: List<Playlist>,
    onEvent: (MusicEvent) -> Unit,
    musicPlaybackUiState: MusicPlaybackUiState,
    playlistUiState: PlaylistUiState,
    playlistViewModel: PlaylistViewModel,
    navController: NavController,
    onNavigateToMusicPlayer: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var showSortSheet by remember {
        mutableStateOf(false)
    }

    var textAdd by remember {
        mutableStateOf("")
    }

    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Playlist",
                        fontWeight = FontWeight.Bold,
                        style = MusicAppTypography.headlineMedium,
                    )
                },
                actions = {
                    PlusButton(
                        icon = Icons.Default.Add,
                        onClick = {
                            showBottomSheet = true
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MusicAppColorScheme.background
                )
            )
        },
    ) { innerPadding ->
        val scrollState = rememberLazyListState()
        val gradientColorList = listOf(
            Color(0xFF000000),
            Color(0xFF6E7B8B)
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
//                                .background(
//                                    brush = gradientBackgroundBrush(
//                                        isLinearGradient = true,
//                                        colors = gradientColorList
//                                    )
//                                )
            ) {
//                SortButton(
//                    icon = Icons.Default.Sort,
//                    onClick = {
//                        showSortSheet = true;
//                    }
//                )
                Spacer(modifier = Modifier.size(10.dp))

                FavoriteItem( onClick = { /*TODO*/ })
                with(playlistUiState) {
                    when (loading) {
                        true -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        false -> {

                            LazyColumn(
                                state = scrollState,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                itemsIndexed(
                                    items = playlist,
                                    key = { _, item -> item.hashCode() }
                                ) { _, item ->
                                    PlaylistItemView(
                                        item = item,
                                        playlistViewModel = playlistViewModel,
                                        navController = navController
                                    )
                                }
                            }

                        }

                        else -> {}
                    }
                }
            }

            /// MiniPlayer
            with(musicPlaybackUiState) {
                if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED) {
                    MusicMiniPlayerCard(
                        modifier = Modifier
                            .padding(5.dp)
                            .offset(y = (-80).dp)
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
        }

    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                    value = textAdd,
                    onValueChange = {
                        textAdd = it
                    },
                    placeholder = {
                        Text(text = "Name of playlist")
                    },
                    label = {
                        Text("")
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        playlistViewModel.createPlaylist(textAdd)
                        textAdd = ""
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                ) {
                    Text("Submit")
                }
            }
        }
    }

//    if (showSortSheet) {
//        ModalBottomSheet(
//            onDismissRequest = {
//                showSortSheet = false
//            },
//            sheetState = sheetState
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Spacer(modifier = Modifier.height(15.dp))
//                Row(
//                    modifier = Modifier
//                        .clickable {
//                            playlistUiState.sort = false
//                            playlistViewModel.getPlaylistsOrderedByName()
//                            scope.launch {
//                                sheetState.hide()
//                            }.invokeOnCompletion {
//                                if (!sheetState.isVisible) {
//                                    showSortSheet = false
//                                }
//                            }
//                        }
//                ) {
//                    Text("+ Sort by name")
//                }
//            }
//        }
//    }
}

@Composable
fun gradientBackgroundBrush(
    isLinearGradient: Boolean,
    colors: List<Color>
): Brush {
    val endOffset = if (isLinearGradient) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }
    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = endOffset
    )
}





