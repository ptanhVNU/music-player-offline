package com.dev.musicplayer.presentation.playlist
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.services.LocalMediaProvider
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.repositories.PlaylistRepositoryImpl
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.use_case.GetPlaylistUseCase
import com.dev.musicplayer.presentation.playlist.listSongOfAlbum.ListSongUiState
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepositoryImpl,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val application: Application
) :  AndroidViewModel(application) {
    var playlistUiState by mutableStateOf(PlaylistUiState())
        private set

    private var _playlist: MutableStateFlow<List<Playlist>> = MutableStateFlow(arrayListOf())
    val playlist: StateFlow<List<Playlist>> = _playlist.asStateFlow()

    private val _playlistsOrderedByName = MutableLiveData<List<Playlist>>()
    val playlistsOrderedByName: LiveData<List<Playlist>> = _playlistsOrderedByName

    fun createPlaylist(title : String) {
        viewModelScope.launch {
            playlistRepository.createPlaylist(title)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlist)
        }
    }


//    private fun toFormattedMusicEntity1(strings: List<String>): List<MusicEntity> {
//        val gson = Gson()
//        val listStringType = object : TypeToken<List<String>>() {}.type
//        val listString: List<String> = gson.fromJson(strings, listStringType)
//
//        for (string in listString) {
//            val musicEntity = gson.fromJson(string, MusicEntity::class.java)
//            println("Artist: ${musicEntity.artist}, Title: ${musicEntity.title}, ID: ${musicEntity.id}")
//        }
//    }


    init {
        getPlaylist()
    }

    private fun getPlaylist() {
        viewModelScope.launch {
            getPlaylistUseCase().catch {
                playlistUiState = playlistUiState.copy(
                    loading = true
                )
            }.collect {
                _playlist.value  = it
                playlistUiState = playlistUiState.copy(
                    loading = false,
                    playlists = _playlist.value
                )
            }
        }
    }

    fun addSongToPlaylist(playlistId: Long, song: MusicEntity) {
        viewModelScope.launch {
            val playlist = playlistRepository.getPlaylistById(playlistId)
            val updatedSongs = playlist.songs?.toMutableList() ?: mutableListOf()
            updatedSongs.add(toFormattedString(song))
            val updatedPlaylist = playlist.copy(songs = updatedSongs)
            playlistRepository.update(updatedPlaylist)
        }
    }

    fun addSongsToPlaylist(playlistId: Long, songs: List<MusicEntity>) {
        viewModelScope.launch {
            val playlist = playlistRepository.getPlaylistById(playlistId)
            val updatedSongs = playlist.songs?.toMutableList() ?: mutableListOf()
            updatedSongs.addAll(songs.map { toFormattedString(it) })
            val updatedPlaylist = playlist.copy(songs = updatedSongs)
            playlistRepository.update(updatedPlaylist)
        }
    }

    private fun toFormattedString(song:MusicEntity): String {
        val gson = Gson()
        println("Formatted JSON String: ${gson.toJson(song)}")
        return gson.toJson(song)
    }


    fun getPlaylistsOrderedByName() {
        viewModelScope.launch {
            try {
                playlistRepository.getPlaylistsOrderedByName().collect { playlists ->
                    _playlistsOrderedByName.postValue(playlists)
                }
            } catch (e: Exception) {
                Log.d("Sort", "Lỗi hàm sort")
            }
        }
    }
}


