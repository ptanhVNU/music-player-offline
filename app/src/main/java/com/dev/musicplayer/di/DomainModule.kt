package com.dev.musicplayer.di

import com.dev.musicplayer.domain.repositories.MusicRepository
import com.dev.musicplayer.domain.repositories.PlaylistRepository
import com.dev.musicplayer.domain.service.PlaybackController
import com.dev.musicplayer.domain.use_case.AddMediaItemsUseCase
import com.dev.musicplayer.domain.use_case.DestroyMediaControllerUseCase
import com.dev.musicplayer.domain.use_case.GetCurrentMusicPositionUseCase
import com.dev.musicplayer.domain.use_case.GetMusicsUseCase
import com.dev.musicplayer.domain.use_case.GetPlaylistUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import com.dev.musicplayer.domain.use_case.SeekMusicPositionUseCase
import com.dev.musicplayer.domain.use_case.SetMediaControllerCallbackUseCase
import com.dev.musicplayer.domain.use_case.SetMusicShuffleEnabledUseCase
import com.dev.musicplayer.domain.use_case.SetPlayerRepeatOneEnabledUseCase
import com.dev.musicplayer.domain.use_case.SkipNextMusicUseCase
import com.dev.musicplayer.domain.use_case.SkipPreviousMusicUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Use SingletonComponent for application-wide scope
object DomainModule {

    @Provides
    @Singleton
    fun provideGetMusicsUseCase(repository: MusicRepository): GetMusicsUseCase {
        return GetMusicsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddMediaItemsUseCase(playbackController: PlaybackController): AddMediaItemsUseCase {
        return AddMediaItemsUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideSetMediaControllerCallbackUseCase(playbackController: PlaybackController): SetMediaControllerCallbackUseCase {
        return SetMediaControllerCallbackUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideDestroyMediaControllerUseCase(playbackController: PlaybackController): DestroyMediaControllerUseCase {
        return DestroyMediaControllerUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideGetCurrentMusicPositionUseCase(playbackController: PlaybackController): GetCurrentMusicPositionUseCase {
        return GetCurrentMusicPositionUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun providePauseMusicUseCase(playbackController: PlaybackController): PauseMusicUseCase {
        return PauseMusicUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun providePlayMusicUseCase(playbackController: PlaybackController): PlayMusicUseCase {
        return PlayMusicUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideResumeMusicUseCase(playbackController: PlaybackController): ResumeMusicUseCase {
        return ResumeMusicUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideSeekMusicPositionUseCase(playbackController: PlaybackController): SeekMusicPositionUseCase {
        return SeekMusicPositionUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideSetMusicShuffleEnabledUseCase(playbackController: PlaybackController): SetMusicShuffleEnabledUseCase {
        return SetMusicShuffleEnabledUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideSetPlayerRepeatEnabledUseCase(playbackController: PlaybackController): SetPlayerRepeatOneEnabledUseCase {
        return SetPlayerRepeatOneEnabledUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideSkipNextMusicUseCase(playbackController: PlaybackController): SkipNextMusicUseCase {
        return SkipNextMusicUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideSkipPreviousMusicUseCase(playbackController: PlaybackController): SkipPreviousMusicUseCase {
        return SkipPreviousMusicUseCase(playbackController)
    }

    @Provides
    @Singleton
    fun provideGetPlaylistUseCase(playlistRepository: PlaylistRepository) : GetPlaylistUseCase {
        return GetPlaylistUseCase(playlistRepository)
    }
}