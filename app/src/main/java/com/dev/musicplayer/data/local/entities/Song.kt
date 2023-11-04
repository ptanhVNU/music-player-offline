package com.dev.musicplayer.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "song",
//    indices = [
//        Index("uri", unique = true)
//    ],
)
data class Song(
    @PrimaryKey(autoGenerate = true) val songId : Long = 0,
    @ColumnInfo(name = "uri") val uri: String,
    // playlistId
    val artistName: String = "Unknown artist",
//    val durationSeconds: Int,
    @ColumnInfo(name = "thumbnail") val thumbnail: String? = null, // song image url
    val title: String, // required
    @ColumnInfo(name = "is_liked") val isLiked: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long, // in milliseconds
) {
    fun toggleLike() = copy(isLiked = !isLiked)
}