package com.dev.musicplayer.core.shared.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.dev.musicplayer.core.services.AudioState
import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.core.services.MusicServiceHandler
import com.dev.musicplayer.core.services.PlayerEvent
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.reposity.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val songDummy = Song(
    songId = 0,
    uri = "",
    title = "",
    createdAt = 0,
)

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AudioViewModel @Inject constructor(
    private val musicServiceHandler: MusicServiceHandler,
    private val repository: MusicRepository,
    private val savedStateHandle: SavedStateHandle,
    private val metaDataReader: MetaDataReader,
    private val application: Application,
) : AndroidViewModel(application) {

    private val _duration = MutableStateFlow<Long>(0L)
    val duration: SharedFlow<Long> = _duration.asSharedFlow()

    private var _progress = MutableStateFlow<Float>(0F)
    private var _progressMillis = MutableStateFlow<Long>(0L)

    private var _progressString: MutableStateFlow<String> = MutableStateFlow("00:00")
    val progressString: SharedFlow<String> = _progressString.asSharedFlow()

    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var notReady = MutableLiveData<Boolean>(true)


    private var _nowPlayingMediaItem = MutableLiveData<MediaItem?>()
    val nowPlayingMediaItem: LiveData<MediaItem?> = _nowPlayingMediaItem

    private var _saveLastPlayedSong: MutableLiveData<Boolean> = MutableLiveData()
    val saveLastPlayedSong: LiveData<Boolean> = _saveLastPlayedSong

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var recentPosition: String = 0L.toString()

    val intent: MutableStateFlow<Intent?> = MutableStateFlow(null)
    private var initJob: Job? = null

    private val audioUris = savedStateHandle.getStateFlow("audioUris", emptyList<Uri>())
    val audioItems = audioUris.map { uris ->
        uris.map { uri ->
//            saveSongToLocal(
            Song(
                title = metaDataReader.getMetaDataFromUri(uri)?.fileName ?: "No name",
                uri = uri.toString(),
                createdAt = Instant.now().toEpochMilli()
            )
//            )
//            loadSongData()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

//    init {
//        loadSongData()
//    }

    fun init() {
        initJob = viewModelScope.launch {
            val job1 = launch {
                musicServiceHandler.audioState.collectLatest { audioState ->
                    when (audioState) {
                        AudioState.Initial -> _uiState.value = UIState.Initial
                        AudioState.Ended -> {
                            _uiState.value = UIState.Ended
                        }

                        is AudioState.Buffering -> {
//                        calculateProgressValue(audioState.progress)
                            notReady.value = true
                        }

                        is AudioState.Playing -> isPlaying = audioState.isPlaying
                        is AudioState.Progress -> {
                            if (_duration.value > 0) {
                                calculateProgressValue(audioState.progress)
                                _progressMillis.value = audioState.progress
                            }
                        }
                        is AudioState.CurrentPlaying -> {
//                        currentSelectedAudio = audioList[audioState.mediaItemIndex]
                        }
                        is AudioState.Ready -> {
                            notReady.value = false
                            _duration.value = audioState.duration
                            calculateProgressValue(musicServiceHandler.getProgress())
                            _uiState.value = UIState.Ready
                        }
                    }
                }
            }

            val job2 = launch {
                musicServiceHandler.nowPlaying.collectLatest { nowPlaying ->
                    if (nowPlaying != null && getCurrentMediaItemIndex() > 0)
                        _nowPlayingMediaItem.postValue(nowPlaying)
                }
            }

            job1.join()
            job2.join()


        }

    }

    fun getCurrentMediaItem(): MediaItem? {
        _nowPlayingMediaItem.value = musicServiceHandler?.getCurrentMediaItem()
        return musicServiceHandler.getCurrentMediaItem()
    }

    fun getCurrentMediaItemIndex(): Int {
        return musicServiceHandler.currentIndex() ?: 0
    }

    @UnstableApi
    fun playMediaItemInMediaSource(index: Int) {
        musicServiceHandler.playMediaItemInMediaSource(index)
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
                    seekPosition = ((_duration.value * uiEvents.position) / 100f).toLong()
                )
            }

            is UIEvents.SelectedAudioChange -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvents.index
                )
            }

            is UIEvents.UpdateProgress -> {
                _progress.value = uiEvents.newProgress
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(
                        uiEvents.newProgress
                    )
                )
            }


        }
    }

    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))

        return String.format("%02d:%02d", minutes, seconds)
    }


    private fun calculateProgressValue(currentProgress: Long) {
        _progress.value =
            if (currentProgress > 0) ((currentProgress.toFloat()) / _duration.value.toFloat()) * 100f
            else 0f
        _progressString.value = formatDuration(currentProgress)
    }



    private var _listSong: MutableStateFlow<List<Song>> = MutableStateFlow(arrayListOf())
    val listSong: StateFlow<List<Song>> = _listSong.asStateFlow()



    override fun onCleared() {
        viewModelScope.launch {
            musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
        Log.w("Check onCleared", "onCleared")

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
    object Ended : UIState()
}