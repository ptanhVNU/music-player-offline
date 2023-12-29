package com.dev.musicplayer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dev.musicplayer.data.local.dao.PlaylistDao
import com.dev.musicplayer.data.local.dao.SongDao
import com.dev.musicplayer.data.local.dao.SongPlaylistsDao
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song


@Database(
    entities = [
        Song::class,
        Playlist::class,
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MusicAppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao

    abstract fun songPlaylistDao() : SongPlaylistsDao
}

