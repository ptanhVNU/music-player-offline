package com.dev.musicplayer.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.presentation.home.components.MusicMiniPlayerCard
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    songs: List<MediaAudioItem>,
    onEvent: (MusicEvent) -> Unit,
    homeUiState: HomeUiState,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateToMusicPlayer: () -> Unit,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
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
    ) {
        val scrollState = rememberLazyListState()

        with(homeUiState) {
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
                                .fillMaxSize()
                                .padding(it),
                            contentPadding = PaddingValues(bottom = 80.dp),
                            state = scrollState,
                        ) {
                            itemsIndexed(
                                songs,
                            ) { index, item ->
                                SongItem(
                                    item = item,
                                    modifier = Modifier.fillParentMaxWidth(),
                                    onItemClicked = {
//                                        val player = ExoPlayer.Builder(context).build()
//                                        val mediaItem = MediaItem.fromUri(item.uri)
//                                        player.setMediaItem(mediaItem)
//                                        player.prepare()
//                                        player.play()
//                                        Log.d("HOME-SCREEN", "absolute path: ${item.absolutePath}")
//                                        onEvent(MusicEvent.OnMusicSelected(item))
//                                        onEvent(MusicEvent.PlayMusic)
                                    },


                                    )
                            }
                        }

                        with(musicPlaybackUiState) {
                            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED)
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

                        }
                    }
                }

                else -> {}
            }
        }
    }
}