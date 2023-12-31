package com.dev.musicplayer.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.presentation.playlist.PlaylistViewModel
import com.dev.musicplayer.presentation.playlist.components.PlaylistContent
import com.dev.musicplayer.presentation.playlist.components.PlaylistItemView
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography


@Composable
fun ResultContent(
    searchResult: SearchResult,
    searchType: SearchType,
    innerPadding: PaddingValues,
    searchViewModel: SearchViewModel,
    musicPlaybackUiState: MusicPlaybackUiState,
    onSongClicked: (MusicEntity) -> Unit,
    onPlaylistClicked: (Playlist) -> Unit,
) {
    val selectedSong by searchViewModel.selectedSong.collectAsState()

    val scrollState = rememberLazyListState()
    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (searchType) {
            SearchType.Songs -> {
                items(
                    items = searchResult.songs,
                    key = { it.id }
                ) { music ->

                    SongItem(
                        isSelected = music == selectedSong,
                        item = music,
                        musicPlaybackUiState = musicPlaybackUiState,
                        onItemClicked = {

                            onSongClicked(music)
                        },
                    )
                }
                if (searchResult.songs.isNotEmpty())
                    item {
                        Text(
                            modifier = Modifier
                                .height(180.dp)
                                .padding(5.dp),
                            text = "Tổng số bài hát: ${searchResult.songs.size}",
                            textAlign = TextAlign.Center,
                            style = MusicAppTypography.headlineMedium,
                        )
                    }
            }

            SearchType.Playlists -> {
                items(
                    items = searchResult.playlists,
                    key = { it.id }
                ) { playlist ->
                    PlaylistContent(
                        album = playlist,
                        onClick = {
                            onPlaylistClicked(playlist)
                        },

                    )
                }
            }
        }


    }
}