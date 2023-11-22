package com.dev.musicplayer.presentation.home

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.dev.musicplayer.core.services.LocalMediaProvider
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.repositories.MusicRepositoryImpl
import com.dev.musicplayer.domain.use_case.AddMediaItemFromStorageUseCase
import com.dev.musicplayer.domain.use_case.GetMusicsUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(SavedStateHandleSaveableApi::class)
class HomeViewModel @Inject constructor(
    private val getMusicsUseCase: GetMusicsUseCase,
//    private val addMediaItemsFromLocalDBUseCase: AddMediaItemsFromLocalDBUseCase,
    private val addMediaItemFromStorageUseCase: AddMediaItemFromStorageUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
    private val musicRepository: MusicRepositoryImpl,
    private val localMediaProvider: LocalMediaProvider,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    var homeUiState by mutableStateOf(HomeUiState())
        private set


    var musicList by savedStateHandle.saveable { mutableStateOf(listOf<MediaAudioItem>()) }

    init {
        getMusicData()
    }

    private fun getMusicData() {
        viewModelScope.launch {
            val musicData = musicRepository.getMediaAudioFromStorage()

            musicList = musicData

            addMediaItemFromStorageUseCase(musicList)
        }
    }

    fun onEvent(event: MusicEvent) {
        when (event) {

            MusicEvent.ResumeMusic -> resumeMusic()

            MusicEvent.PauseMusic -> pauseMusic()

            is MusicEvent.OnMusicSelected -> {
                homeUiState = homeUiState.copy(selectedMusic = event.selectedMusic)
            }

            MusicEvent.PlayMusic -> playMusic()


        }
    }


    public fun deleteSong(song: Song) {
        viewModelScope.launch {
            musicRepository.deleteSong(song)
        }
    }

    private fun playMusic() {
        homeUiState.apply {
            playMusicUseCase(musicList.indexOf(selectedMusic))
        }


    }

    private fun resumeMusic() {
        resumeMusicUseCase()
    }

    private fun pauseMusic() {
        pauseMusicUseCase()
    }
}

