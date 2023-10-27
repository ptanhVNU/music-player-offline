package com.dev.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Upsert
    suspend fun insertSong(song: SongEntity)

    @Delete
    suspend fun deleteSong(song: SongEntity)

    @Query("SELECT * FROM song ORDER BY title ASC")
    fun getSongsOrderedByName() : Flow<List<SongEntity>>

    @Query("SELECT * FROM song ORDER BY createdAt ASC")
    fun getSongsOrderedByCreatedAt() : Flow<List<SongEntity>>


}