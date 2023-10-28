package com.dev.musicplayer.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId: Long = 0,
    // playlistId
    val artistName: List<String>? = null,
    val durationSeconds: Int,
    val thumbnails: String? = null, // song image url
    val title: String, // required
    val isLiked : Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Int, // in milliseconds
) {
    fun toggleLike() = copy(isLiked = !isLiked)
}