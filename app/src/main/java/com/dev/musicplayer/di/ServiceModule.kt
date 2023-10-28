package com.dev.musicplayer.di

import android.app.Application
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import com.dev.musicplayer.services.MetaDataReader
import com.dev.musicplayer.services.MetaDataReaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import java.io.File

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideAudioAttributes() =
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

    @ServiceScoped
    @Provides
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes,
    ) = ExoPlayer.Builder(context).apply {
        setAudioAttributes(audioAttributes, true)
        /// Sets whether the player should pause automatically
        // when audio is rerouted from a headset to device speakers.
        setHandleAudioBecomingNoisy(true)
    }

    @Provides
    @ServiceScoped
    fun provideDataSourceFactory(
        @ApplicationContext context: Context
    ) = DefaultDataSource.Factory(context)


    @Provides
    @ViewModelScoped
    fun provideMetaDataReader(app: Application): MetaDataReader {
        return MetaDataReaderImpl(app)
    }

    @Provides
    @ServiceScoped
    @UnstableApi
    // Creates a `CacheDataSource.Factory` that uses a `SimpleCache` to cache data from a `DefaultDataSource.Factory`.
    fun provideCacheDataSourceFactory(
        @ApplicationContext context: Context,
        dataSource: DefaultDataSource.Factory
    ): CacheDataSource.Factory {
        // Creates a `File` object for the cache directory.
        val cacheDir = File(context.cacheDir, "media")

        // Creates a `StandaloneDatabaseProvider` object to provide a database connection to the `SimpleCache`.
        val databaseProvides = StandaloneDatabaseProvider(context)

        // Creates a `SimpleCache` object to cache data.
        val cache = SimpleCache(cacheDir, NoOpCacheEvictor(), databaseProvides)

        return CacheDataSource.Factory().apply {
            setCache(cache)
            setUpstreamDataSourceFactory(dataSource)
        }
    }

}