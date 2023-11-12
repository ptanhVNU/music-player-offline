package com.dev.musicplayer.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val thumbnail: String? = null, // image url
    val songs: List<String>? = null, //
    @ColumnInfo(name = "created_at") val createdAt: Int,
)