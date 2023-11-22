package com.dev.musicplayer.core.shared.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.domain.use_case.DestroyMediaControllerUseCase
import com.dev.musicplayer.domain.use_case.GetCurrentMusicPositionUseCase
import com.dev.musicplayer.domain.use_case.SetMediaControllerCallbackUseCase
import com.dev.musicplayer.utils.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val setMediaControllerCallbackUseCase: SetMediaControllerCallbackUseCase,
    private val getCurrentMusicPositionUseCase: GetCurrentMusicPositionUseCase,
    private val destroyMediaControllerUseCase: DestroyMediaControllerUseCase
) : ViewModel() {
    var musicPlaybackUiState by mutableStateOf(MusicPlaybackUiState())
        private set

    init {
        setMediaControllerCallback()
    }

    private fun setMediaControllerCallback() {
        setMediaControllerCallbackUseCase { playerState, currentMusic, currentPosition, totalDuration,
                                            isShuffleEnabled, isRepeatOneEnabled ->
            musicPlaybackUiState = musicPlaybackUiState.copy(
                playerState = playerState,
                currentMusic = currentMusic,
                currentPosition = currentPosition,
                totalDuration = totalDuration,
                isShuffleEnabled = isShuffleEnabled,
                isRepeatOneEnabled = isRepeatOneEnabled
            )

            Log.d("SHARED-VIEW-MODEL", "Player state: $playerState")

            if (playerState == PlayerState.PLAYING) {
                viewModelScope.launch {
                    while (true) {
                        delay(duration = 1.seconds)
                        musicPlaybackUiState = musicPlaybackUiState.copy(
                            currentPosition = getCurrentMusicPositionUseCase()
                        )
                    }
                }
            }
        }
    }

    fun destroyMediaController() {
        destroyMediaControllerUseCase()
    }
}