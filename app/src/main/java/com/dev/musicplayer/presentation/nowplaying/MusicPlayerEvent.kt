package com.dev.musicplayer.presentation.nowplaying

sealed class MusicPlayerEvent {
    data object ResumeMusic : MusicPlayerEvent()
    data object PauseMusic : MusicPlayerEvent()
    data object SkipNextMusic : MusicPlayerEvent()
    data object SkipPreviousMusic : MusicPlayerEvent()
    data class SeekMusicPosition(val musicPosition: Long) : MusicPlayerEvent()
    data class SetMusicShuffleEnabled(val isShuffleEnabled: Boolean) : MusicPlayerEvent()
    data class SetPlayerRepeatOneEnabled(val isRepeatOneEnabled: Boolean) : MusicPlayerEvent()
}
