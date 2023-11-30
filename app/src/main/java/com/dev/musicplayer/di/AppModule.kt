package com.dev.musicplayer.di


import android.app.Application
import android.content.Context

import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.core.services.MetaDataReaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class
)
object AppModule {
//    @Provides

//    @Singleton
//    fun provideMetaDataReader(app: Application): MetaDataReader {
////        return MetaDataReaderImpl(app)
//
//    }

    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}
