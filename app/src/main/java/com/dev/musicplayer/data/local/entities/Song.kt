package com.dev.musicplayer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val songId: Int = 0,
    // playlistId
//    val artistName: List<String>? = null,
    val duration: String,
    val durationSeconds: Int,
    val thumbnails: String? = null, // song image url
    val title: String, // required
    val isLiked : Boolean = false,
    val createdAt: Int, // in milliseconds
) {
    fun toggleLike() = copy(isLiked = !isLiked)
}