package com.dev.musicplayer.data.local.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.Playlist
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Upsert
    fun createPlaylist(playlist: Playlist)
    @Query("DELETE from playlist WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long?)

    @Query("SELECT songs FROM playlist WHERE id = :playlistId")
    suspend fun getListOfSongs(playlistId: Long): List<String>

    @Update
    suspend fun update(playlist: Playlist)

    @Query("UPDATE Playlist SET songs = :updatedListSong WHERE id = :playlistId")
    suspend fun updateListOfSongs(playlistId: Long, updatedListSong: List<String>)

    fun isJsonStringEqual(jsonString1: String, jsonString2: String): Boolean {
        val gson = Gson()
        val obj1 = gson.fromJson(jsonString1, Any::class.java)
        val obj2 = gson.fromJson(jsonString2, Any::class.java)
        return obj1 == obj2
    }

    suspend fun deleteSongFromPlaylist(playlistId: Long, musicEntityJson: String) {
//        val listOfSongs = getListOfSongs(playlistId)
//
//        val mutableListOfSongs = mutableListOf<String>()
//        mutableListOfSongs.addAll(listOfSongs)
//
//        val iterator = mutableListOfSongs.iterator()
//        while (iterator.hasNext()) {
//            val song = iterator.next()
//            println(song)
//            if (isJsonStringEqual(song, musicEntityJson)) {
////                iterator.remove()
//                break
//            }
//        }
//        updateListOfSongs(playlistId, mutableListOfSongs)
    }

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<Playlist>>


    @Query("SELECT songs FROM playlist WHERE id = :playlistId")
    fun getSongsOfPlaylist(playlistId: Long): Flow<List<String>>


    @Query("SELECT * FROM playlist ORDER BY title ASC")
    fun getPlaylistsOrderedByName() : Flow<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY created_at ASC")
    fun getPlaylistsOrderedByCreatedAt() : Flow<List<Playlist>>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): Playlist

    @Query("SELECT * FROM playlist WHERE title = :title")
    suspend fun getPlaylistByName(title : String): Playlist

    @Query("SELECT * FROM playlist WHERE title LIKE '%' || :query || '%'")
    suspend fun searchPlaylists(query: String): List<Playlist>

    @Query("SELECT title FROM playlist WHERE id = :playlistId")
    suspend fun getNameByID(playlistId: Long): String
}