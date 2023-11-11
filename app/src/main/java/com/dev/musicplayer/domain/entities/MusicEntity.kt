package com.dev.musicplayer.domain.entities

data class MusicEntity(
    val id: String,
    val title: String,
    val artist: String,
    val source: String,
    val image: String?,
    val isLiked: Boolean = false,
    val createdAt: Long,
) {

}