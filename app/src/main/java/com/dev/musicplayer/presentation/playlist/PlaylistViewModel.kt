package com.dev.musicplayer.presentation.playlist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.ext.toFormattedMusicEntity
import com.dev.musicplayer.core.ext.toFormattedString
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.repositories.PlaylistRepositoryImpl
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.use_case.AddMediaItemsUseCase
import com.dev.musicplayer.domain.use_case.GetPlaylistUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.home.MusicEvent
import com.dev.musicplayer.presentation.search.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepositoryImpl,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
) : ViewModel() {
    var playlistUiState by mutableStateOf(PlaylistUiState())
        private set

    private var _allPlaylists: MutableStateFlow<List<Playlist>> = MutableStateFlow(arrayListOf())
    val allPlaylists: StateFlow<List<Playlist>> = _allPlaylists.asStateFlow()

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist.asStateFlow()

    private val _playlistsOrderedByName = MutableLiveData<List<Playlist>>()
    val playlistsOrderedByName: LiveData<List<Playlist>> = _playlistsOrderedByName

    private var songsPlaylistUiState by mutableStateOf(SongsPlaylistUiState())

    private val _selectedSong = MutableStateFlow<MusicEntity?>(null)
    val selectedSong: StateFlow<MusicEntity?> = _selectedSong.asStateFlow()

    fun setSelectedSong(song: MusicEntity?) {
        _selectedSong.value = song
    }

    companion object {
        const val TAG = "Playlist VIEW MODEL"
    }

    fun createPlaylist(title: String) {
        viewModelScope.launch {
            playlistRepository.createPlaylist(title)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlist)
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
                _allPlaylists.value = it
                playlistUiState = playlistUiState.copy(
                    loading = false,
                    playlists = _allPlaylists.value
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
            _playlist.value = playlistRepository.getPlaylistById(playlistId)
        }
    }

    private val _songs = MutableStateFlow<List<MusicEntity>>(emptyList())
    val songs: StateFlow<List<MusicEntity>> = _songs.asStateFlow()

    fun getSongsByPlaylistId(playlistId: Long): List<MusicEntity> {

        viewModelScope.launch {
            _playlist.value = playlistRepository.getPlaylistById(playlistId)
            if (playlist.value != null) {


                _songs.value = playlist.value?.songs?.map {
                    toFormattedMusicEntity(it)
                } ?: emptyList()


                songsPlaylistUiState = songsPlaylistUiState.copy(
                    loading = false,
                    musics = _songs.value
                )

            }
        }

        return _songs.value
    }

    fun addMusicItems(musics: List<MusicEntity>) {
        viewModelScope.launch {
            addMediaItemsUseCase(musics)
        }
    }

    fun onEvent(event: MusicEvent) {
        when (event) {
            MusicEvent.PlayMusic -> playMusic()

            MusicEvent.ResumeMusic -> resumeMusic()

            MusicEvent.PauseMusic -> pauseMusic()

            is MusicEvent.OnMusicSelected -> {
                Log.d("Search View Model", "on music selected: ${event.selectedMusic}")

                songsPlaylistUiState =
                    songsPlaylistUiState.copy(selectedMusic = event.selectedMusic)
            }
        }
    }

    private fun playMusic() {
        songsPlaylistUiState.apply {
            musics?.indexOf(selectedMusic)?.let {
                Log.d(TAG, "playMusic: $it")
                playMusicUseCase(it)
            }
        }
    }

    fun initiatePlaybackFromBeginning() {
        songsPlaylistUiState.apply {
            musics?.indexOf(musics.first()).let {
                Log.d(TAG, "playMusic: $it")
                playMusicUseCase(it ?: 0)
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
        Log.d("Playlist VIEW MODEL", "onCleared: Playlist view model ")
        super.onCleared()
    }

}

