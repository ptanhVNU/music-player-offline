package com.dev.musicplayer.presentation.playlist

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.home.components.MusicMiniPlayerCard
import com.dev.musicplayer.presentation.playlist.components.PlaylistItemView
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    playlist: List<Playlist>,
    onEvent: (MusicEvent) -> Unit,
    musicPlaybackUiState: MusicPlaybackUiState,
    playlistUiState: PlaylistUiState,
    albumViewModel: AlbumViewModel,
    navController: NavController,
    onNavigateToMusicPlayer: () -> Unit,
) {
    var text by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    var textAdd by remember {
        mutableStateOf("")
    }

    var activeSort by remember {
        mutableStateOf(false)
    }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
//                return super.onPostFling(consumed, available)
//            }
//        }
//    }

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MusicAppColorScheme.background)
            )
        },
    ) {
        val scrollState = rememberLazyListState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)

        ) {
            SortButton(
                icon = Icons.Default.Sort,
                onClick = {
                    activeSort = true;
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
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

                        Box {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 80.dp),
                                state = scrollState,
                            ) {
                                itemsIndexed(
                                    items = playlist,
                                    key = { _, item -> item.hashCode() }
                                ) { _, item ->
                                    PlaylistItemView(
                                        item = item,
                                        albumViewModel = albumViewModel,
                                        navController = navController
                                    )
                                }
                            }

                            with(musicPlaybackUiState) {
                                Log.d("TAG", "PlaylistScreen: $musicPlaybackUiState")
//                                if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED) {
                                    MusicMiniPlayerCard(
                                        /// TODO: Impl progress bar
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .offset(y = screenHeight - 100.dp),
                                        music = currentMusic,
                                        playerState = playerState,
                                        onResumeClicked = { onEvent(MusicEvent.ResumeMusic) },
                                        onPauseClicked = { onEvent(MusicEvent.PauseMusic) },
                                        onClick = { onNavigateToMusicPlayer() }
                                    )
//                                }
                            }
                        }
                    }
                    else -> {}
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
                        albumViewModel.createPlaylist(textAdd)
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
}




