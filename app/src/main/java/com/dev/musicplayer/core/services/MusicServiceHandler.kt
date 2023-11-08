package com.dev.musicplayer.core.services

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@UnstableApi
class MusicServiceHandler constructor(
    val player: ExoPlayer,
    private val mediaSession: MediaSession,
//    var coroutineScope: LifecycleCoroutineScope,
    private val context: Context,
) : Player.Listener {


    private var job: Job? = null

    private val _audioState: MutableStateFlow<AudioState> =
        MutableStateFlow(AudioState.Initial)
    val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

    private var _nowPlaying = MutableStateFlow(player.currentMediaItem)
    val nowPlaying = _nowPlaying.asSharedFlow()

    private val _nextTrackAvailable = MutableStateFlow<Boolean>(false)
    val nextTrackAvailable = _nextTrackAvailable.asSharedFlow()

    private val _previousTrackAvailable = MutableStateFlow<Boolean>(false)
    val previousTrackAvailable = _previousTrackAvailable.asSharedFlow()

    private var _currentSongIndex = MutableStateFlow<Int>(0)
    val currentSongIndex = _currentSongIndex.asSharedFlow()


    init {
        player.addListener(this)
        job = Job()

        _nowPlaying.value = player.currentMediaItem
    }

    fun addMediaItem(mediaItem: MediaItem) {
        player.addMediaItem(mediaItem)
        player.prepare()
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        player.addMediaItems(mediaItems)
        player.prepare()
    }

    private fun updateNextPreviousTrackAvailability() {
        _nextTrackAvailable.value = player.hasNextMediaItem()
        _previousTrackAvailable.value = player.hasPreviousMediaItem()
    }

    fun getMediaItemWithIndex(index: Int): MediaItem {
        return player.getMediaItemAt(index)
    }

    fun removeMediaItem(position: Int) {
        player.removeMediaItem(position)
        _currentSongIndex.value = currentIndex()
    }

    private fun addMediaItemNotSet(mediaItem: MediaItem) {
        player.addMediaItem(mediaItem)
        if (player.mediaItemCount == 1) {
            player.prepare()
            player.playWhenReady = true
        }
        updateNextPreviousTrackAvailability()
    }

    fun clearMediaItems() {
        player.clearMediaItems()
    }

    fun addMediaItemList(mediaItemList: List<MediaItem>) {
        for (mediaItem in mediaItemList) {
            addMediaItemNotSet(mediaItem)
        }
        Log.d("Media Item List", "addMediaItemList: ${player.mediaItemCount}")
    }

    fun playMediaItemInMediaSource(index: Int) {
        player.seekTo(index, 0)
        player.prepare()
        player.playWhenReady = true
    }

    fun addMediaItem(mediaItem: MediaItem, playWhenReady: Boolean = true) {
        player.clearMediaItems()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = playWhenReady
    }

    fun moveMediaItem(fromIndex: Int, newIndex: Int) {
        player.moveMediaItem(fromIndex, newIndex)
        _currentSongIndex.value = currentIndex()
    }

    suspend fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedAudioIndex: Int = -1,
        seekPosition: Long = 0,
    ) {

        when (playerEvent) {
            PlayerEvent.Backward -> player.seekBack()
            PlayerEvent.Forward -> player.seekForward()
            PlayerEvent.SeekToNext -> player.seekToNext()
            PlayerEvent.PlayPause -> playOrPause()
            PlayerEvent.SeekTo -> player.seekTo(seekPosition)
            PlayerEvent.SelectedAudioChange -> {
                when (selectedAudioIndex) {
                    player.currentMediaItemIndex -> {
                        playOrPause()
                    }

                    else -> {
                        player.seekToDefaultPosition(selectedAudioIndex)
                        _audioState.value = AudioState.Playing(isPlaying = true)
                        player.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            PlayerEvent.Stop -> {
                stopProgressUpdate()
                player.stop()
            }

            is PlayerEvent.UpdateProgress -> {
                player.seekTo(
                    (player.duration * playerEvent.newProgress).toLong()
                )
            }

            else -> {}
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value =
                AudioState.Buffering(player.currentPosition)

            ExoPlayer.STATE_READY -> _audioState.value =
                AudioState.Ready(player.duration)

            Player.STATE_ENDED -> {
                TODO()
            }

            Player.STATE_IDLE -> {
                TODO()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {

        _audioState.value = AudioState.Playing(isPlaying = isPlaying)
        _audioState.value = AudioState.CurrentPlaying(player.currentMediaItemIndex)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    override fun onTracksChanged(tracks: Tracks) {
        Log.d("Tracks", "onTracksChanged: ${tracks.groups.size}")
        super.onTracksChanged(tracks)
    }

    override fun onPlayerError(error: PlaybackException) {
        when(error.errorCode) {
            PlaybackException.ERROR_CODE_TIMEOUT -> {
                Log.e("Player Error", "onPlayerError: ${error.message}")
                //TODO: show toast
                player.pause()
            }
            else -> {
                Log.e("Player Error", "onPlayerError: ${error.message}")
                //TODO: show toast
                player.pause()
            }
        }
    }
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Log.w("Smooth Switching Transition", "Current Position: ${player.currentPosition}")
//        mayBeNormalizeVolume()
        Log.w("REASON", "onMediaItemTransition: $reason")
        Log.d("Media Item Transition", "Media Item: ${mediaItem?.mediaMetadata?.title}")
        _nowPlaying.value = mediaItem
        updateNextPreviousTrackAvailability()
//        updateNotification()
    }

    private suspend fun playOrPause() {
        if (player.isPlaying) {
            player.pause()
            stopProgressUpdate()
        } else {
            player.play()
            _audioState.value = AudioState.Playing(
                isPlaying = true
            )
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _audioState.value = AudioState.Progress(player.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value = AudioState.Playing(isPlaying = false)
    }

    fun currentIndex(): Int {
        return player.currentMediaItemIndex
    }

    fun getCurrentMediaItem(): MediaItem? {
        return player.currentMediaItem
    }

    fun seekTo(position: String)  {
        player.seekTo(position.toLong())
        Log.d("Check seek", "seekTo: ${player.currentPosition}")
    }
    fun skipSegment(position: Long) {
        if (position in 0..player.duration) {
            player.seekTo(position)
        }
        else if (position > player.duration) {
            player.seekToNext()
        }
    }

    fun release() {
        player.stop()
        player.playWhenReady = false
        player.removeListener(this)
        if (job?.isActive == true) {
            job?.cancel()
            job = null
        }
        Log.w("Service", "Check job: ${job?.isActive}")

    }

    fun getPlayerDuration(): Long {
        return player.duration
    }

    fun getProgress(): Long {
        return player.currentPosition
    }


}

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object SelectedAudioChange : PlayerEvent()
    object Backward : PlayerEvent()
    object SeekToNext : PlayerEvent()
    object Forward : PlayerEvent()
    object SeekTo : PlayerEvent()
    object Next : PlayerEvent()
    object Previous : PlayerEvent()
    object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

sealed class RepeatState {
     object None : RepeatState()
     object All : RepeatState()
     object One : RepeatState()
}

sealed class AudioState {
    object Initial : AudioState()
    object Ended : AudioState()
    data class Ready(val duration: Long) : AudioState()
    data class Progress(val progress: Long) : AudioState()
    data class Buffering(val progress: Long) : AudioState()
    data class Playing(val isPlaying: Boolean) : AudioState()
    data class CurrentPlaying(val mediaItemIndex: Int) : AudioState()

}