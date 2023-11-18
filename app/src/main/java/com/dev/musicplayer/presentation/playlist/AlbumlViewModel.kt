package com.dev.musicplayer.presentation.playlist
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.repositories.PlaylistRepository
import com.dev.musicplayer.domain.use_case.GetPlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val getPlaylistUseCase: GetPlaylistUseCase
) : ViewModel() {

    //lấy trạng thái từ UIState
    var playlistUiState by mutableStateOf(PlaylistUiState())
        private set

    private var _playlist: MutableStateFlow<List<Playlist>> = MutableStateFlow(arrayListOf())
    val playlist: StateFlow<List<Playlist>> = _playlist.asStateFlow()

    private val _album = MutableStateFlow<Playlist?>(null)
    val album: StateFlow<Playlist?> = _album.asStateFlow()


    //Tạo thêm một album mới
    fun createPlaylist(title : String) {
        viewModelScope.launch {
            playlistRepository.createPlaylist(title)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            Log.d("Delete", "This is a debug message.")
            playlistRepository.deletePlaylist(playlist)
        }
    }

    init {
        getPlaylist()
    }

    //lấy danh sách album
    fun getPlaylist() {
        viewModelScope.launch {
            getPlaylistUseCase().catch {
                playlistUiState = playlistUiState.copy(
                    loading = false,
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

    fun getPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            val result = playlistRepository.getPlaylistById(playlistId)
            _album.value = result
        }
    }

    //lắng nghe sự kiện của UI
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
