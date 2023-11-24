package com.dev.musicplayer.di

import android.content.Context
import com.dev.musicplayer.core.services.LocalMediaProvider
import com.dev.musicplayer.core.services.MusicPlaybackController
import com.dev.musicplayer.data.local.repositories.MusicRepositoryImpl
import com.dev.musicplayer.data.local.repositories.PlaylistRepositoryImpl
import com.dev.musicplayer.data.local.store.PlaylistStore
import com.dev.musicplayer.data.local.store.SongStore
import com.dev.musicplayer.domain.repositories.MusicRepository
import com.dev.musicplayer.domain.repositories.PlaylistRepository
import com.dev.musicplayer.domain.service.PlaybackController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideMusicRepository(
        songStore: SongStore,
        localMediaProvider: LocalMediaProvider
    ): MusicRepository {
        return MusicRepositoryImpl(songStore, localMediaProvider)
    }

    @Provides
    @Singleton
    fun provideLocalMediaProvider(@ApplicationContext context: Context): LocalMediaProvider {
        return LocalMediaProvider(context)
    }


    @Provides
    @Singleton
    fun providePlaylistRepository(playListStore: PlaylistStore): PlaylistRepository {
        return PlaylistRepositoryImpl(playListStore)
    }


    @Provides
    @Singleton

    fun provideMusicPlaybackController(
        @ApplicationContext context: Context,
    ): PlaybackController {
        return MusicPlaybackController(context)
    }
}