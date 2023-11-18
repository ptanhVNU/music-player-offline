package com.dev.musicplayer.domain.entities

data class PlaylistEntity(
    val id: Long,
    val title: String,
    val image: String?,
    val songs: List<String>?,
    val createdAt: Long? = null,
) {

}