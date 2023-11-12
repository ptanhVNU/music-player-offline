package com.dev.musicplayer.di

import android.content.Context
import com.dev.musicplayer.core.services.MusicPlaybackController
import com.dev.musicplayer.data.local.repositories.MusicRepositoryImpl
import com.dev.musicplayer.data.local.store.SongStore
import com.dev.musicplayer.domain.repositories.MusicRepository
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
    fun provideMusicRepository(songStore: SongStore) : MusicRepository {
        return MusicRepositoryImpl(songStore)
    }

    @Provides
    @Singleton
    fun provideMusicPlaybackController(
        @ApplicationContext context: Context,
    ) : PlaybackController {
        return MusicPlaybackController(context)
    }
}