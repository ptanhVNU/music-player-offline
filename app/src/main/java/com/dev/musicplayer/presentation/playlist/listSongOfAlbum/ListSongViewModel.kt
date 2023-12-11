package com.dev.musicplayer.presentation.playlist.listSongOfAlbum

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.repositories.PlaylistRepositoryImpl
import com.dev.musicplayer.domain.entities.MusicEntity
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class ListSongViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepositoryImpl,
    private val application: Application
) :  AndroidViewModel(application) {

    private var playlistId: Long = 0

    private var _song: MutableStateFlow<List<MusicEntity>> = MutableStateFlow(arrayListOf())
    val song: StateFlow<List<MusicEntity>> = _song.asStateFlow()

    private val _album = MutableStateFlow<Playlist?>(null)
    val album: StateFlow<Playlist?> = _album.asStateFlow()



    var listSongUiState by mutableStateOf(ListSongUiState())
        private set

    fun setPlaylistId(playlistId: Long) {
        this.playlistId = playlistId
        getPlaylistById(playlistId)
        getSong(playlistId)
    }

    init {
    }

    fun toFormattedMusicEntity(string: String): MusicEntity {
        val gson = Gson()
        return gson.fromJson(string, MusicEntity::class.java)
    }


    private fun getPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            val result = playlistRepository.getPlaylistById(playlistId)
            _album.value = result
        }
    }

    private fun getSong(playlistId: Long) {
        viewModelScope.launch {
//            try {
                val resultFlow = playlistRepository.getSongsOfPlaylist(playlistId)
//                val musicEntitiesFlow: Flow<List<MusicEntity>> = resultFlow.map { listOfJsonStrings ->
//                    val music = mutableListOf<MusicEntity>()
//                    listOfJsonStrings.forEach { jsonString ->
//                        try {
//                            val musicEntity = gson.fromJson(jsonString, MusicEntity::class.java)
//                            music.add(musicEntity)
//                        } catch (e: Exception) {
//                            if (e is JsonSyntaxException) {
//                                try {
//                                    val jsonArray = JsonParser().parse(jsonString).asJsonArray
//                                    for (element in jsonArray) {
//                                        val musicEntity = gson.fromJson(element, MusicEntity::class.java)
//                                        println("$musicEntity")
//                                        music.add(musicEntity)
//                                    }
//                                } catch (ex: JsonSyntaxException) {
//                                    println("Lỗi")
//                                }
//                            } else {
//                            }
//                        }
//                    }
//                    music.toList()
//                }
//
//                musicEntitiesFlow.collect { musicEntities ->
//                    _song.value = musicEntities
//                }
//
//                listSongUiState.copy(
//                    loading = false
//                )
//            } catch (e: Exception) {
//                listSongUiState.copy(
//                    loading = true
//                )
//            }
            val musicEntities: List<MusicEntity>? = album.value?.songs?.let { songs ->
                songs.map {
                    toFormattedMusicEntity(it)
                }
            }
            val musicListToShow: List<MusicEntity> = musicEntities ?: listOf()
            musicListToShow.let {song ->
                _song.value = song
            }

            listSongUiState.copy(
                loading = false
            )
        }
    }
}
