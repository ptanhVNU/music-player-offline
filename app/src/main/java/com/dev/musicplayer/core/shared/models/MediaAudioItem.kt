package com.dev.musicplayer.core.shared.models

import android.graphics.Bitmap
import android.net.Uri

data class MediaAudioItem(
    val name: String,
    val absolutePath: String,
    val artist: String,
    val id: Long,
    val uri: Uri,
    val size: Long,
    val duration: Long,
    val artWork: Bitmap?,
)

