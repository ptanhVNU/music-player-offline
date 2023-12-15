package com.dev.musicplayer.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.playlist.PlaylistViewModel
import com.dev.musicplayer.presentation.utils.MusicMiniPlayerCard
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.utils.PlayerState

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    homeViewModel: HomeViewModel,
    playlistViewModel: PlaylistViewModel,
    onEvent: (MusicEvent) -> Unit,
    onPlaylistClicked: (playlist: Playlist) -> Unit,
    onNavigateToMusicPlayer: () -> Unit,
    musicPlaybackUiState: MusicPlaybackUiState,
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()
    val searchType by viewModel.searchType.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = viewModel::updateQuery,
                currentType = searchType,
                onSearchTypeSelect = viewModel::updateType,
                onClearRequest = viewModel::clearQueryText
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (searchResult.errorMsg != null) {
//                                FullScreenSadMessage(searchResult.errorMsg)
            } else {
                ResultContent(
                    searchResult = searchResult,
                    innerPadding = innerPadding,
                    musicPlaybackUiState = musicPlaybackUiState,
                    searchType = searchType,
                    onSongClicked = { music ->
                        viewModel.setSelectedSong(music)

                        onEvent(MusicEvent.OnMusicSelected(music))
                        onEvent(MusicEvent.PlayMusic)

                        homeViewModel.setSelectedSong(null)
                        playlistViewModel.setSelectedSong(null)
                    },
                    searchViewModel = viewModel,
                    onPlaylistClicked = onPlaylistClicked,
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
                        musicPlaybackUiState = musicPlaybackUiState
                    )
                }
            }


        }
    }
}