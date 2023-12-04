package com.dev.musicplayer.core.shared.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.DataStoreManager
import com.dev.musicplayer.domain.use_case.DestroyMediaControllerUseCase
import com.dev.musicplayer.domain.use_case.GetCurrentMusicPositionUseCase
import com.dev.musicplayer.domain.use_case.SetMediaControllerCallbackUseCase
import com.dev.musicplayer.presentation.search.SearchResult
import com.dev.musicplayer.presentation.search.SearchType
import com.dev.musicplayer.utils.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val setMediaControllerCallbackUseCase: SetMediaControllerCallbackUseCase,
    private val getCurrentMusicPositionUseCase: GetCurrentMusicPositionUseCase,
    private val destroyMediaControllerUseCase: DestroyMediaControllerUseCase,
    private val manager: DataStoreManager,
) : ViewModel() {
    var musicPlaybackUiState by mutableStateOf(MusicPlaybackUiState())
        private set

    init {
        setMediaControllerCallback()
    }

    private fun setMediaControllerCallback() {
        setMediaControllerCallbackUseCase { playerState, currentMusic, currentPosition, totalDuration,
                                            isShuffleEnabled, isRepeatOneEnabled ->
            musicPlaybackUiState = musicPlaybackUiState.copy(
                playerState = playerState,
                currentMusic = currentMusic,
                currentPosition = currentPosition,
                totalDuration = totalDuration,
                isShuffleEnabled = isShuffleEnabled,
                isRepeatOneEnabled = isRepeatOneEnabled
            )
            Log.d("Shared View Model", "music playback: $musicPlaybackUiState")

            if (playerState == PlayerState.PLAYING) {
                viewModelScope.launch {
                    while (true) {
                        delay(1.seconds)
                        musicPlaybackUiState = musicPlaybackUiState.copy(
                            currentPosition = getCurrentMusicPositionUseCase()
                        )
                    }
                }
            }
        }
    }

    fun destroyMediaController() {
        destroyMediaControllerUseCase()
    }

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _searchType = MutableStateFlow(SearchType.Songs)
    val searchType = _searchType.asStateFlow()

    val searchResult = _query
        .combine(searchType) { query, type ->
            val trimmedQuery = query.trim()
            if (trimmedQuery.isEmpty()) {
                SearchResult()
            } else {
                when (type) {
                    SearchType.Songs -> SearchResult(songs = manager.querySearch.searchSongs(trimmedQuery))
//                    SearchType.Albums -> SearchResult(albums = manager.querySearch.searchAlbums(trimmedQuery))
//                    SearchType.Artists -> SearchResult(artists = manager.querySearch.searchArtists(trimmedQuery))
//                    SearchType.AlbumArtists -> SearchResult(albumArtists = manager.querySearch.searchAlbumArtists(trimmedQuery))
//                    SearchType.Composers -> SearchResult(composers = manager.querySearch.searchComposers(trimmedQuery))
//                    SearchType.Lyricists -> SearchResult(lyricists = manager.querySearch.searchLyricists(trimmedQuery))
//                    SearchType.Genres -> SearchResult(genres = manager.querySearch.searchGenres(trimmedQuery))
                    SearchType.Playlists -> SearchResult(playlists = manager.querySearch.searchPlaylists(trimmedQuery))
                }
            }
        }.catch { exception ->
            Timber.e(exception)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchResult()
        )
}