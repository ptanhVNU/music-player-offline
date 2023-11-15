package com.dev.musicplayer.domain.entities

import com.dev.musicplayer.data.local.entities.Song

data class PlaylistEntity(
    val id: String,
    val title: String,
    val image: String?,
    val songs: List<String>?,
    val createdAt: Long? = null,
) {

}