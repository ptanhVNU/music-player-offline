package com.dev.musicplayer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.musicplayer.data.local.dao.SongDao
import com.dev.musicplayer.data.local.entities.SongEntity

@Database(
    entities = [SongEntity::class],
    version = 1,
)
abstract class MusicAppDatabase : RoomDatabase() {
    abstract val dao: SongDao
}