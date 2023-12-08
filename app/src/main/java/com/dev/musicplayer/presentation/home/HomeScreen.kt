package com.dev.musicplayer.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.home.components.PlaylistBottomSheet
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.presentation.utils.MusicMiniPlayerCard
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onEvent: (MusicEvent) -> Unit,
    playlists: List<Playlist>,
    homeUiState: HomeUiState,
    addMediaItem: (musics: List<MusicEntity>) -> Unit,
    onAddToPlaylist: (playlist : Playlist, song: MusicEntity) -> Unit,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateToMusicPlayer: () -> Unit,
    pullRefreshState: PullRefreshState,
    isLoading: Boolean,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    var selectedMusicIndex by remember { mutableIntStateOf(-1) }

    var selectedMusic by remember { mutableStateOf<MusicEntity?>(null) }

    val sheetState = rememberModalBottomSheetState()

    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "JetMusic",
                        fontWeight = FontWeight.Bold,
                        style = MusicAppTypography.headlineMedium,
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(containerColor = MusicAppColorScheme.background)

            )
        }
    ) { innerPadding ->
        val scrollState = rememberLazyListState()


        with(homeUiState) {
            when {
                loading == true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    }
                }

                loading == false && errorMessage == null -> {
                    if (musics != null) {
                        Box(
                            modifier = Modifier
                                .pullRefresh(pullRefreshState)
                                .fillMaxSize(),
                        ) {

                            LazyColumn(
                                state = scrollState,
                                modifier = Modifier.padding(innerPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(musics, key = { music ->
                                    music.id
                                }) { music ->
                                    val isSelected = selectedMusicIndex == musics.indexOf(music)

                                    SongItem(
                                        item = music,
                                        isSelected = isSelected,
                                        musicPlaybackUiState = musicPlaybackUiState,
                                        onItemClicked = {
                                            addMediaItem(musics)

                                            if (!isSelected) {
                                                selectedMusicIndex = musics.indexOf(music)
                                            }

                                            onEvent(MusicEvent.OnMusicSelected(music))
                                            onEvent(MusicEvent.PlayMusic)
                                        },
                                        onAddToPlaylist = {
                                            Log.d("TAG", "HomeScreen song: ${music.id} ")
                                            selectedMusic = music
                                            isSheetOpen = true
                                        }
                                    )
                                }

                                item {
                                    Text(
                                        modifier = Modifier
                                            .height(180.dp)
                                            .padding(5.dp)
                                            .align(Alignment.CenterStart),
                                        text = "Tổng số bài hát: ${musics.size}",
                                        textAlign = TextAlign.Center,
                                        style = MusicAppTypography.headlineMedium,
                                    )
                                }
                            }

                            PullRefreshIndicator(
                                refreshing = isLoading,
                                state = pullRefreshState,
                                scale = true,
                                modifier = Modifier.align(Alignment.TopCenter),
                                contentColor = MusicAppColorScheme.primary
                            )


                            if (isSheetOpen) {
                                PlaylistBottomSheet(
                                    playlists = playlists,
                                    onDismissRequest = { isSheetOpen = false },
                                    bottomSheetState = sheetState,
                                    onClicked = {
                                        Log.d("TAG", "HomeScreen: playlist id ${it.id}")
                                        onAddToPlaylist(it, selectedMusic!!)
                                        isSheetOpen = false

                                    }
                                )
                            }

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
                                    )
                                }
                            }
                        }
                    }
                }

                errorMessage != null -> {
                    LaunchedEffect(snackBarHostState) {
                        snackBarHostState.showSnackbar(errorMessage)
                    }
                }
            }
        }
    }
}

