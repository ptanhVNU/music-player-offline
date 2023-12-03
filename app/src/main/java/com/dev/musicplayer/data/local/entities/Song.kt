package com.dev.musicplayer.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "song",
    indices = [
        Index("uri", unique = true)
    ],
)
data class Song(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val songId : Long = 0,
    @ColumnInfo(name = "uri") val uri: String,
    // playlistId
    val artistName: String = "Unknown artist",
//    val durationSeconds: Int,
    @ColumnInfo(name = "thumbnail") var thumbnail: String? = null, // song image url
    val title: String, // required
    @ColumnInfo(name = "is_liked") val isLiked: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = Instant.now().toEpochMilli(), // in milliseconds
) {
    fun toggleLike() = copy(isLiked = !isLiked)
}

