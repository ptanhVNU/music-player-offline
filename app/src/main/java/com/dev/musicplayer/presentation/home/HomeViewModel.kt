package com.dev.musicplayer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.domain.use_case.AddMediaItemsUseCase
import com.dev.musicplayer.domain.use_case.GetMusicsUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMusicsUseCase: GetMusicsUseCase,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
    val savedStateHandle: SavedStateHandle,

    ) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    init {
        getMusicData()
    }

    private fun getMusicData() {
        homeUiState = homeUiState.copy(loading = true)
        viewModelScope.launch {
            getMusicsUseCase().catch {
                homeUiState = homeUiState.copy(
                    loading = false,
                    errorMessage = "Error"
                )
            }.collect {

                addMediaItemsUseCase(it)

                homeUiState = homeUiState.copy(
                    loading = false,
                    musics = it
                )
            }
        }
    }

    fun onEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.PlayMusic ->
                playMusic()


            MusicEvent.ResumeMusic -> resumeMusic()

            MusicEvent.PauseMusic -> pauseMusic()

            is MusicEvent.OnMusicSelected -> {
                homeUiState = homeUiState.copy(selectedMusic = event.selectedMusic)
            }
        }
    }


    private fun playMusic() {
        homeUiState.apply {
            musics?.indexOf(selectedMusic)?.let {
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

