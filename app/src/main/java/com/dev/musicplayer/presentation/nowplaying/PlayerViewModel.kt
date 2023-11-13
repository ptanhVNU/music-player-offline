package com.dev.musicplayer.presentation.nowplaying

import androidx.lifecycle.ViewModel
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import com.dev.musicplayer.domain.use_case.SeekMusicPositionUseCase
import com.dev.musicplayer.domain.use_case.SetMusicShuffleEnabledUseCase
import com.dev.musicplayer.domain.use_case.SetPlayerRepeatOneEnabledUseCase
import com.dev.musicplayer.domain.use_case.SkipNextMusicUseCase
import com.dev.musicplayer.domain.use_case.SkipPreviousMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
    private val seekMusicPositionUseCase: SeekMusicPositionUseCase,
    private val skipNextMusicUseCase: SkipNextMusicUseCase,
    private val skipPreviousMusicUseCase: SkipPreviousMusicUseCase,
    private val setMusicShuffleEnabledUseCase: SetMusicShuffleEnabledUseCase,
    private val setPlayerRepeatOneEnabledUseCase: SetPlayerRepeatOneEnabledUseCase
) : ViewModel() {
    fun onEvent(event: MusicPlayerEvent) {
        when (event) {
            MusicPlayerEvent.ResumeMusic -> resumeMusic()

            MusicPlayerEvent.PauseMusic -> pauseMusic()

            MusicPlayerEvent.SkipNextMusic -> skipNextMusic()

            MusicPlayerEvent.SkipPreviousMusic -> skipPreviousMusic()

            is MusicPlayerEvent.SeekMusicPosition -> seekToMusicPosition(event.musicPosition)

            is MusicPlayerEvent.SetMusicShuffleEnabled -> setMusicShuffleEnabled(event.isShuffleEnabled)

            is MusicPlayerEvent.SetPlayerRepeatOneEnabled -> setPlayerRepeatOneEnabled(event.isRepeatOneEnabled)
            else -> {}
        }
    }

    private fun resumeMusic() {
        resumeMusicUseCase()
    }

    private fun pauseMusic() {
        pauseMusicUseCase()
    }

    private fun seekToMusicPosition(position: Long) {
        seekMusicPositionUseCase(position)
    }

    private fun skipNextMusic() {
        skipNextMusicUseCase()
    }

    private fun skipPreviousMusic() {
        skipPreviousMusicUseCase()
    }

    private fun setMusicShuffleEnabled(isEnabled: Boolean) {
        setMusicShuffleEnabledUseCase(isEnabled)
    }

    private fun setPlayerRepeatOneEnabled(isRepeatOneEnabled: Boolean) {
        setPlayerRepeatOneEnabledUseCase(isRepeatOneEnabled)
    }
}