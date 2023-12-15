package com.dev.musicplayer.domain.entities

data class MusicEntity(
    val id: String,
    val title: String,
    val artist: String,
    val source: String,
    val image: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val otherMusic = other as MusicEntity

        return id == otherMusic.id &&
                title == otherMusic.title &&
                artist == otherMusic.artist &&
                source == otherMusic.source &&
                image == otherMusic.image
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
