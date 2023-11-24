package com.dev.musicplayer.core.ext

import androidx.media3.common.MediaItem
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.domain.entities.MusicEntity

fun MediaAudioItem.toMusicEntity() =
    MusicEntity(
        id = id.toString(),
        title = name,
        artist = artist,
        source = uri.toString(),
        image = ""
    )


fun MediaItem.toMusicEntity() =
    MusicEntity(
        id = mediaId,
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.artist.toString(),
        source = mediaId,
        image = mediaMetadata.artworkUri.toString()
    )