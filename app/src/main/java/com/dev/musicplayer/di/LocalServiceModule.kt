package com.dev.musicplayer.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dev.musicplayer.R
import com.dev.musicplayer.core.common.DB_NAME
import com.dev.musicplayer.core.notifications.MusicNotificationManager
import com.dev.musicplayer.core.services.MusicServiceHandler
import com.dev.musicplayer.data.local.Converters
import com.dev.musicplayer.data.local.MusicAppDatabase
import com.dev.musicplayer.data.local.dao.PlaylistDao
import com.dev.musicplayer.data.local.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalServiceModule {

    @Provides
    @Singleton
    fun provideMusicAppDatabase(
        @ApplicationContext context: Context,

        ): MusicAppDatabase = Room.databaseBuilder(
        context,
        MusicAppDatabase::class.java,
        DB_NAME,
    ).addTypeConverter(Converters()).build()


    @Provides
    @Singleton
    fun provideSongDao(musicAppDatabase: MusicAppDatabase): SongDao = musicAppDatabase.songDao()

    @Provides
    @Singleton
    fun providePlaylistDao(musicAppDatabase: MusicAppDatabase): PlaylistDao =
        musicAppDatabase.playlistDao()

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context

    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun provideAudioAttributes() : AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

    @Singleton
    @Provides
    @UnstableApi
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes,
    ) : ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()


    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer,
    ): MediaSession = MediaSession.Builder(context, player).build()

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer,
    ): MusicNotificationManager = MusicNotificationManager(
        context = context,
        exoPlayer = player
    )

    @Provides
    @Singleton
    fun provideServiceHandler(exoPlayer: ExoPlayer): MusicServiceHandler =
        MusicServiceHandler(exoPlayer)
}