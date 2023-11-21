package com.dev.musicplayer.presentation.home

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.services.LocalMediaProvider
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.repositories.MusicRepositoryImpl
import com.dev.musicplayer.domain.use_case.AddMediaItemsUseCase
import com.dev.musicplayer.domain.use_case.GetMusicsUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMusicsUseCase: GetMusicsUseCase,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
    private val musicRepository: MusicRepositoryImpl,
    private val localMediaProvider: LocalMediaProvider,
    private val application: Application,
) : AndroidViewModel(application) {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    private val _videoItemsStateFlow = localMediaProvider.getMediaVideosFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val videoItemsStateFlow = _videoItemsStateFlow



    init {

//        videoItemsStateFlow.collect()
//        addMediaItemsUseCase()
    }




    fun onEvent(event: MusicEvent) {
        when (event) {

//            HomeEvent.PlayMusic -> playMusic()

            MusicEvent.ResumeMusic -> resumeMusic()

            MusicEvent.PauseMusic -> pauseMusic()

            is MusicEvent.OnMusicSelected -> {
                homeUiState = homeUiState.copy(selectedMusic = event.selectedMusic)
            }

            else -> {}
        }
    }



    public fun deleteSong(song: Song) {
        viewModelScope.launch {
            musicRepository.deleteSong(song)
        }
    }

//    private fun playMusic() {
//
//        homeUiState.apply {
//            _listSong.value.indexOf(selectedMusic).let {
//                playMusicUseCase(it)
//            }
//        }
//    }

    private fun resumeMusic() {
        resumeMusicUseCase()
    }

    private fun pauseMusic() {
        pauseMusicUseCase()
    }
}

