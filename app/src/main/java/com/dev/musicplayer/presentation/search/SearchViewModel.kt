package com.dev.musicplayer.presentation.search

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.data.local.DataStoreManager
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val context: Application,
    private val manager: DataStoreManager,
) : ViewModel() {

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

    fun clearQueryText() {
        _query.update { "" }
    }

    fun updateQuery(query: String) {
        _query.update { query }
    }

    fun updateType(type: SearchType) {
        _searchType.update { type }
    }

    fun setQueue(songs: List<Song>?, startPlayingFromIndex: Int = 0) {
        if (songs == null) return
        manager.setQueue(songs, startPlayingFromIndex)
        Toast.makeText(context, "Playing", Toast.LENGTH_SHORT).show()
    }

    fun handleClick(song: Song){
        setQueue(listOf(song))
    }

    fun handleClick(playlist: Playlist){
    }
}