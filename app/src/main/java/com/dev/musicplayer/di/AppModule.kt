package com.dev.musicplayer.di

import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.core.services.MetaDataReaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(
    ViewModelComponent::class
)
object AppModule {
    @Provides
    @ViewModelScoped
    fun provideMetaDataReader(): MetaDataReader {
        return MetaDataReaderImpl()
    }
}
