package com.dev.musicplayer.core.shared.models

import android.graphics.Bitmap
import android.net.Uri
import com.dev.musicplayer.data.local.entities.Song

data class MediaAudioItem(
    val name: String,
    val absolutePath: String,
    val artist: String,
    val id: Long,
    val uri: Uri,
    val size: Long,
    val duration: Long,
    val dateModified: Long,
    val artWork: Bitmap?,


) {
    fun Long.toHhMmSs(): String {
        val seconds = (this / 1000).toInt()
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    fun MediaAudioItem.toSong() : Song {
        return Song(
            songId = id,
            title = name,
            uri = uri.toString(),
        )
    }
}

