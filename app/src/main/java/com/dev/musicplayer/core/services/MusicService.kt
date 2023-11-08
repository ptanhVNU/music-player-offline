package com.dev.musicplayer.core.services

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.audio.SonicAudioProcessor
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.dev.musicplayer.MainActivity
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MusicService : MediaSessionService() {

    lateinit var player: ExoPlayer

    lateinit var mediaSession: MediaSession

    private val binder = MusicBinder()

//  @Inject
//    lateinit var notificationManager: MusicNotificationManager

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        Log.w("Service", "Media Session Service Created")

        player = ExoPlayer.Builder(this)
            .setAudioAttributes(provideAudioAttributes(), true)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .setHandleAudioBecomingNoisy(true)
            .setSeekForwardIncrementMs(5000)
            .setSeekBackIncrementMs(5000)
            .setMediaSourceFactory(provideMediaSourceFactory(this))
            .setRenderersFactory(provideRendererFactory(this))
            .build()

        mediaSession = provideMediaSession(
            this,
            player,
        )

        val sessionToken = SessionToken(this, ComponentName(this, MusicService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({ controllerFuture.get() }, MoreExecutors.directExecutor())
    }

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        notificationManager.startNotificationService(
//            mediaSession = mediaSession,
//            mediaSessionService = this
//        )
        return super.onStartCommand(intent, flags, startId)
    }

    inner class MusicBinder : Binder(){
        val service: MusicService
            get() = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder =
        super.onBind(intent) ?: binder

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
        mediaSession

    @UnstableApi
    private fun release() {
        mediaSession.run {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.release()
            }
        }
    }
    @UnstableApi
    override fun onDestroy() {
        super.onDestroy()
        release()
        Log.d("MediaService", "onDestroy ...... ")
    }

    @UnstableApi
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("MediaService", "onTaskRemoved: ")
        release()
        stopSelf()

    }

    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    @UnstableApi
    fun provideMediaSourceFactory(context: Context): DefaultMediaSourceFactory =
        DefaultMediaSourceFactory(context)

    @UnstableApi
    fun provideRendererFactory(context: Context): DefaultRenderersFactory =
        object : DefaultRenderersFactory(context) {
            override fun buildAudioSink(
                context: Context,
                enableFloatOutput: Boolean,
                enableAudioTrackPlaybackParams: Boolean,
                enableOffload: Boolean
            ): AudioSink {
                return DefaultAudioSink.Builder(context)
                    .setEnableFloatOutput(enableFloatOutput)
                    .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
                    .setOffloadMode(if (enableOffload) DefaultAudioSink.OFFLOAD_MODE_ENABLED_GAPLESS_REQUIRED else DefaultAudioSink.OFFLOAD_MODE_DISABLED)
                    .setAudioProcessorChain(
                        DefaultAudioSink.DefaultAudioProcessorChain(
                            emptyArray(),
                            SilenceSkippingAudioProcessor(2_000_000, 20_000, 256),
                            SonicAudioProcessor()
                        )
                    )
                    .build()
            }
        }

    fun provideMediaSession(
        context: Context,
        player: ExoPlayer,

    ): MediaSession =
        MediaSession.Builder(context, player)
            .setSessionActivity(
                PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()



}