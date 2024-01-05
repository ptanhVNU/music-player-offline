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
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.repositories.MusicRepositoryImpl
import com.dev.musicplayer.data.local.repositories.PlaylistRepositoryImpl
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.use_case.GetPlaylistUseCase
import com.dev.musicplayer.presentation.playlist.listSongOfAlbum.ListSongUiState
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepositoryImpl,
    private val musicRepositoryImpl: MusicRepositoryImpl,
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
                    loading = true,
                    sort = true
                )
            }.collect {
                _playlist.value  = it
                playlistUiState = playlistUiState.copy(
                    loading = false,
                    sort = true,
                    playlists = _playlist.value,

                )
            }
        }
    }

    private fun getLikedSongs() {
        viewModelScope.launch {
            val songsList = musicRepositoryImpl.getLikedSongs().toList()
            songsList.map { song ->
                val musicEntityList = song.map { item ->
                    item.thumbnail?.let {
                        MusicEntity(
                            id = item.songId.toString(),
                            title = item.title,
                            artist = item.artistName,
                            source = item.uri,
                            image = it
                        )
                    }
                }
                addSongsToFavoritePlaylist(musicEntityList)
            }
            Log.d("test", "truy ván thành công")
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

    private fun addSongsToFavoritePlaylist(songs: List<MusicEntity?>) {
        viewModelScope.launch {
            val playlist = playlistRepository.getPlaylistByName("Favorites")
            val updatedSongs = playlist.songs?.toMutableList() ?: mutableListOf()
            val songStrings = songs.filterNotNull().map { toFormattedString(it) }
            updatedSongs.addAll(songStrings)
            val updatedPlaylist = playlist.copy(songs = updatedSongs)
            playlistRepository.update(updatedPlaylist)
        }
    }

    private fun toFormattedString(song:MusicEntity): String {
        val gson = Gson()
        return gson.toJson(song)
    }


    fun getPlaylistsOrderedByName() {
        viewModelScope.launch {
            try {
                playlistRepository.getPlaylistsOrderedByName().collect { playlists ->
                    _playlistsOrderedByName.postValue(playlists)
                }
                playlistUiState = playlistUiState.copy(
                    sort = true,
                    playlists = _playlistsOrderedByName.value,
                )
            } catch (e: Exception) {
                Log.d("Sort", "Lỗi hàm sort")
            }
        }
    }

    fun createFavoritePlaylist() {
        val playlistFlow: Flow<List<Playlist>> = playlistRepository.getAllPlaylists()
        viewModelScope.launch {
            playlistFlow.collect { playlists ->
                if (playlists.isEmpty()) {
                    playlistRepository.createPlaylist(title = "Favorites")
                    getLikedSongs()
                }
            }
        }
    }
}


