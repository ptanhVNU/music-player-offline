package com.dev.musicplayer.core.shared.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.dev.musicplayer.core.services.AudioState
import com.dev.musicplayer.core.services.MusicServiceHandler
import com.dev.musicplayer.core.services.PlayerEvent
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.reposity.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val songDummy = Song(
    songId = 0,
    uri = "",
    title = "",
    createdAt = 0,
)

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val musicServiceHandler: MusicServiceHandler,
    private val repository: MusicRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var currentSelectedAudio by savedStateHandle.saveable { mutableStateOf(songDummy) }
    var audioList by savedStateHandle.saveable { mutableStateOf(listOf<Song>()) }

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        loadSongData()
    }

    init {
        viewModelScope.launch {
            musicServiceHandler.audioState.collectLatest { audioState ->
                when (audioState) {
                    AudioState.Initial -> _uiState.value = UIState.Initial
                    is AudioState.Buffering -> calculateProgressValue(audioState.progress)
                    is AudioState.Playing -> isPlaying = audioState.isPlaying
                    is AudioState.Progress -> calculateProgressValue(audioState.progress)
                    is AudioState.CurrentPlaying -> {
                        currentSelectedAudio = audioList[audioState.mediaItemIndex]
                    }

                    is AudioState.Ready -> {
                        duration = audioState.duration
                        _uiState.value = UIState.Ready
                    }
                }


            }
        }
    }


    private fun loadSongData() {
        viewModelScope.launch {
            repository.getAllSongs().catch { exception ->
                println(exception.toString())
            }.collect {
                val songs = if (it.isNullOrEmpty()) emptyList<Song>() else it
                audioList = songs
                setMediaItems()
            }

        }
    }

    private fun setMediaItems() {
        audioList.map { audio ->
            MediaItem.Builder()
                .setUri(audio.uri.toUri())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audio.artistName)
                        .setDisplayTitle(audio.title)
                        .build()
                ).build()
        }.also {
            musicServiceHandler.setMediaItemList(it)
        }
    }


    private fun calculateProgressValue(currentProgress: Long) {
        progress =
            if (currentProgress > 0) ((currentProgress.toFloat()) / duration.toFloat()) * 100f
            else 0f
        progressString = formatDuration(currentProgress)
    }

    fun onUiEvents(uiEvents: UIEvents) = viewModelScope.launch {
        when (uiEvents) {
            UIEvents.Backward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.SeekToNext -> musicServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is UIEvents.PlayPause -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.PlayPause
                )
            }

            is UIEvents.SeekTo -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = ((duration * uiEvents.position) / 100f).toLong()
                )
            }

            is UIEvents.SelectedAudioChange -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvents.index
                )
            }

            is UIEvents.UpdateProgress -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(
                        uiEvents.newProgress
                    )
                )
            }


        }
    }

    private fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)

        return String.format("%02d:%02d", minute, seconds)
    }

    override fun onCleared() {
        viewModelScope.launch {
            musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
    }
}

sealed class UIEvents {
    object PlayPause : UIEvents()
    data class SelectedAudioChange(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    object SeekToNext : UIEvents()
    object Backward : UIEvents()
    object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}