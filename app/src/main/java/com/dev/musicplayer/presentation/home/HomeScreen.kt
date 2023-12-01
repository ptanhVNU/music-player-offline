package com.dev.musicplayer.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.presentation.utils.MusicMiniPlayerCard
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onEvent: (MusicEvent) -> Unit,
    homeUiState: HomeUiState,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateToMusicPlayer: () -> Unit,
    onSearchClicked: () -> Unit,
    pullRefreshState: PullRefreshState,
    isLoading: Boolean,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    var selectedMusicIndex by remember { mutableIntStateOf(-1) }

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
                actions = {
                    IconButton(
                        onClick = {
                            //TODO: Implement search bar
                            onSearchClicked()
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search music",
                        )
                    }
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
                                            selectedMusicIndex = if (isSelected) -1 else musics.indexOf(music)
                                            onEvent(MusicEvent.OnMusicSelected(music))
                                            onEvent(MusicEvent.PlayMusic)
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

