package com.dev.musicplayer.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.presentation.home.components.MusicMiniPlayerCard
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEvent: (MusicEvent) -> Unit,
    homeUiState: HomeUiState,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateToMusicPlayer: () -> Unit,

) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        onClick = {},
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
                        CircularProgressIndicator()
                    }
                }

                loading == false && errorMessage == null -> {
                    if (musics != null) {
                        Box {
                            LazyColumn(
                                state = scrollState,
                                modifier = Modifier.padding(innerPadding),
                                contentPadding = PaddingValues(bottom = 80.dp)
                            ) {
                                items(musics) {
                                    SongItem(
                                        item = it,
                                        onItemClicked = {
                                            onEvent(MusicEvent.OnMusicSelected(it))
                                            onEvent(MusicEvent.PlayMusic)

                                        }
                                    )
                                }
                            }

                            with(musicPlaybackUiState) {
                                if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED) {
                                    MusicMiniPlayerCard(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .align(Alignment.BottomCenter),
                                        music = currentMusic,
                                        playerState = playerState,
                                        onResumeClicked = { onEvent(MusicEvent.ResumeMusic) },
                                        onPauseClicked = { onEvent(MusicEvent.PauseMusic) },
                                        onClick = { onNavigateToMusicPlayer() }
                                    )
                                }
                            }
                        }
                    }
                }

                errorMessage != null -> {
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(errorMessage)
                    }
                }
            }
        }
    }
}