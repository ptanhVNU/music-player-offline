package com.dev.musicplayer.presentation.search

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.data.local.DataStoreManager
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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

     fun setQueue(songs: List<Song>?, startPlayingFromIndex: Int = 0) {
        if (songs == null) return
        manager.setQueue(songs, startPlayingFromIndex)
        Toast.makeText(context, "Playing", Toast.LENGTH_SHORT).show()
    }

    fun handleClick(song: MusicEntity){
//        setQueue(listOf(song))
    }

    fun handleClick(playlist: Playlist){
    }
}