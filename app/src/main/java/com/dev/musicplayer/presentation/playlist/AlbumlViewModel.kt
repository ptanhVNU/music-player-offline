package com.dev.musicplayer.presentation.playlist
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.Uri
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
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.repositories.PlaylistRepositoryImpl
import com.dev.musicplayer.domain.use_case.GetPlaylistUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepositoryImpl,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val application: Application,
    private val localMediaProvider: LocalMediaProvider
) :  AndroidViewModel(application) {
    var playlistUiState by mutableStateOf(PlaylistUiState())
        private set

    private var _playlist: MutableStateFlow<List<Playlist>> = MutableStateFlow(arrayListOf())
    val playlist: StateFlow<List<Playlist>> = _playlist.asStateFlow()

    private val _album = MutableStateFlow<Playlist?>(null)
    val album: StateFlow<Playlist?> = _album.asStateFlow()

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

    private fun addSongToPlaylist(playlistId: Long, song: Song) {
        viewModelScope.launch {
            val playlist = playlistRepository.getPlaylistById(playlistId)
            val updatedSongs = playlist.songs?.toMutableList() ?: mutableListOf()
            updatedSongs.add(toFormattedString(song))
            val updatedPlaylist = playlist.copy(songs = updatedSongs)
            playlistRepository.update(updatedPlaylist)
        }
    }

    fun selectMusicFromStorage(playlistId: Long, uris: List<Uri>) {
        viewModelScope.launch((Dispatchers.IO)) {
            for(uri: Uri in uris) {
                val mediaAudioItem = localMediaProvider.getSongItemFromContentUri(uri)
                Log.d("Tên nhạc", "{$mediaAudioItem}")
                if (mediaAudioItem != null) {
                    val song = Song(
                        uri = mediaAudioItem.uri.toString(),
                        title = mediaAudioItem.name
                    )
                    addSongToPlaylist(playlistId, song)
                }
            }
        }
    }

//

//    fun selectMusicFromStorage(playlistId: Long, uris: List<Uri>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            for (uri: Uri in uris) {
//                application.contentResolver.let { contentResolver ->
//                    val songMetaData = metaDataReader.getMetaDataFromUri(uri, contentResolver)
//                    if (songMetaData != null) {
//                        val song = Song(
//                            uri = songMetaData.uri.toString(),
//                            title = songMetaData.fileName,
//                        )
//                        addSongToPlaylist(playlistId, song)
//                    }
//                }
//            }
//        }
//    }

    private fun toFormattedString(song:Song): String {
        val gson = Gson()
        return gson.toJson(song)
    }

    fun toFormattedSong(string:String): Song {
        val gson = Gson()
        return gson.fromJson(string, Song::class.java)
    }

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

    fun getPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            val result = playlistRepository.getPlaylistById(playlistId)
            _album.value = result
        }
    }

    fun onPlaylistEvent(event: PlaylistEvent) {
        playlistUiState = when (event) {
            is PlaylistEvent.SelectedPlaylist -> {
                playlistUiState.copy(selectedPlaylist = event.selectedPlaylist)
            }

            is PlaylistEvent.SwipeTdoDelete -> {
                playlistUiState.copy(deletedPlaylist = event.deletedPlaylist)
            }
        }
    }
}
