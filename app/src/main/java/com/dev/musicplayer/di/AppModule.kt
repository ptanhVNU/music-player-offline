package com.dev.musicplayer.di

import android.app.Application
import android.content.Context
import androidx.media3.datasource.DefaultDataSource
import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.core.services.MetaDataReaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(
    ServiceComponent::class,
    ViewModelComponent::class,
)
object AppModule {
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

}