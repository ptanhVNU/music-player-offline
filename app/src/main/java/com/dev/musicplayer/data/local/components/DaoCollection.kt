package com.dev.musicplayer.data.local.components

import com.dev.musicplayer.data.local.dao.PlaylistDao
import com.dev.musicplayer.data.local.dao.SongDao
import javax.inject.Inject

data class DaoCollection @Inject constructor(
    val songDao: SongDao,
//    val albumDao: AlbumDao,
//    val artistDao: ArtistDao,
//    val albumArtistDao: AlbumArtistDao,
//    val composerDao: ComposerDao,
//    val lyricistDao: LyricistDao,
//    val genreDao: GenreDao,
    val playlistDao: PlaylistDao,
//    val blacklistDao: BlacklistDao
)