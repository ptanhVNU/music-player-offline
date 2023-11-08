package com.dev.musicplayer.presentation.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.reposity.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val metaDataReader: MetaDataReader,
    private val musicRepository: MusicRepository,
) : ViewModel() {


    private val _selectedSongFileName = MutableLiveData<String>()
    private val selectedSongFileName: LiveData<String> get() = _selectedSongFileName

    private var _songEntity: MutableLiveData<Song> = MutableLiveData()
    val songEntity: LiveData<Song> = _songEntity

    private var _listSong: MutableStateFlow<List<Song>> = MutableStateFlow(arrayListOf())
    val listSong: StateFlow<List<Song>> = _listSong.asStateFlow()

    val listJob: MutableStateFlow<ArrayList<Song>> = MutableStateFlow(arrayListOf())

    init {
        fetchSongs()

//        player.prepare()
    }


    fun selectMusicFromStorage(uris: List<Uri>) {
        for (uri: Uri in uris) {
            println("uri: ${uri.toString()}")
            val songMetaData = metaDataReader.getMetaDataFromUri(uri)
            if (songMetaData != null) {
                _selectedSongFileName.value = songMetaData.fileName ?: "Unknown"
                _songEntity.value =
                    Song(
                        title = selectedSongFileName.value ?: "Unknown",
                        createdAt = Instant.now().toEpochMilli(),
                        uri = uri.toString(),
                    )

                // lưu vào local db
                if (_songEntity.value != null) {
                    insertSong(_songEntity.value!!)
                }
//                fetchSongs()
            }
        }


    }

    private fun insertSong(song: Song) {
        viewModelScope.launch {
            musicRepository.insertSong(song)
        }

    }

     private fun fetchSongs() {
        viewModelScope.launch {
            musicRepository.getAllSongs().catch { exception -> println(exception) }.collect {
                _listSong.value = if (it.isNullOrEmpty()) emptyList() else it
            }

        }
    }
}

