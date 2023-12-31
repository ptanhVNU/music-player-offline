package com.dev.musicplayer.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.repositories.MusicRepository
import com.dev.musicplayer.domain.use_case.AddMediaItemsUseCase
import com.dev.musicplayer.domain.use_case.GetMusicsUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMusicsUseCase: GetMusicsUseCase,
    private val musicRepository: MusicRepository,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    companion object {
        const val TAG = "HOME VIEW MODEL"
    }

    init {
        getMusicData()
    }

    override fun onCleared() {
        super.onCleared()
        musicRepository.cancelJobs()
    }


    private val _songs = MutableStateFlow<List<MusicEntity>>(emptyList())
    val songs: StateFlow<List<MusicEntity>> = _songs.asStateFlow()

    private val _selectedSong = MutableStateFlow<MusicEntity?>(null)
    val selectedSong: StateFlow<MusicEntity?> = _selectedSong.asStateFlow()

    fun setSelectedSong(song: MusicEntity?) {
        _selectedSong.value = song
    }

    fun getMusicData() {
        homeUiState = homeUiState.copy(loading = true)
        viewModelScope.launch {
            delay(500)
            getMusicsUseCase().catch {
                homeUiState = homeUiState.copy(
                    loading = false,
                    errorMessage = "Error"
                )
            }.collect {

                addMediaItemsUseCase(it)
                _songs.value = it
                homeUiState = homeUiState.copy(
                    loading = false,
                    musics = it
                )
            }
        }
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
                Log.d(TAG, "on music selected: ${event.selectedMusic}")

                homeUiState = homeUiState.copy(selectedMusic = event.selectedMusic)
            }
        }
    }


    private fun playMusic() {
        homeUiState.apply {
            musics?.indexOf(selectedMusic)?.let {
                Log.d(TAG, "playMusic: $it")
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
}

