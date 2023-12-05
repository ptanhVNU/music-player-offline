package com.dev.musicplayer.presentation.search

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.data.local.DataStoreManager
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.use_case.AddMediaItemsUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import com.dev.musicplayer.presentation.home.HomeUiState
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.home.MusicEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val manager: DataStoreManager,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _searchType = MutableStateFlow(SearchType.Songs)
    val searchType = _searchType.asStateFlow()

    var searchUiState by mutableStateOf(SearchUiState())
        private set

    val searchResult = _query
        .combine(searchType) { query, type ->
            val trimmedQuery = query.trim()
            if (trimmedQuery.isEmpty()) {
                SearchResult()
            } else {
                when (type) {
                    SearchType.Songs -> {
                        val songs = manager.querySearch.searchSongs(trimmedQuery)

                        searchUiState = searchUiState.copy(
                            loading = false,
                            musics = songs
                        )
                        viewModelScope.launch {

                                delay(500)
                                addMediaItemsUseCase(songs)

                        }
                        SearchResult(songs = songs )
                    }
                    SearchType.Playlists -> SearchResult(playlists = manager.querySearch.searchPlaylists(trimmedQuery))
                }
            }
        }.catch { exception ->
           Log.e("Search View Model", exception.toString())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchResult()
        )

    fun clearQueryText() {
        _query.update { "" }
    }

    fun updateQuery(query: String) {
        _query.update { query }
    }

    fun updateType(type: SearchType) {
        _searchType.update { type }
    }

    fun onEvent(event: MusicEvent) {
        when (event) {
            MusicEvent.PlayMusic -> playMusic()

            MusicEvent.ResumeMusic -> resumeMusic()

            MusicEvent.PauseMusic -> pauseMusic()

            is MusicEvent.OnMusicSelected -> {
                Log.d("Search View Model", "on music selected: ${event.selectedMusic}")

                searchUiState = searchUiState.copy(selectedMusic = event.selectedMusic)
            }
        }
    }

    private fun playMusic() {
        searchUiState.apply {
            musics?.indexOf(selectedMusic)?.let {
                Log.d(HomeViewModel.TAG, "playMusic: $it")
                playMusicUseCase(it)
            }
        }

    }

    private fun resumeMusic() {
        resumeMusicUseCase()
    }

    private fun pauseMusic() {
        pauseMusicUseCase()
    }

    override fun onCleared() {
        Log.d("SEARCH VIEW MODEL", "onCleared: Search view model ")
        super.onCleared()
    }

    //
}