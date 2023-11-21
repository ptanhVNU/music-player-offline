package com.dev.musicplayer.di

import android.content.Context
import com.dev.musicplayer.core.services.LocalMediaProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(
    ViewModelComponent::class
)
object AppModule {
    @Provides
    @ViewModelScoped
    fun provideLocalMediaProvider(@ApplicationContext context: Context): LocalMediaProvider {
        return LocalMediaProvider(context)
    }
}
