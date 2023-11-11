package com.dev.musicplayer.presentation.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.repositories.MusicRepositoryImpl
import com.dev.musicplayer.domain.entities.MusicEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val metaDataReader: MetaDataReader,
    private val musicRepository: MusicRepositoryImpl,

    ) : ViewModel() {


    private val _selectedSongFileName = MutableLiveData<String>()
    private val selectedSongFileName: LiveData<String> get() = _selectedSongFileName

    private var _listSong: MutableStateFlow<List<MusicEntity>> = MutableStateFlow(arrayListOf())
    val listSong: StateFlow<List<MusicEntity>> = _listSong.asStateFlow()

    val listJob: MutableStateFlow<ArrayList<Song>> = MutableStateFlow(arrayListOf())

    init {
        fetchSongs()
    }


    fun selectMusicFromStorage(uris: List<Uri>) {
        for (uri: Uri in uris) {
            println("uri: ${uri.toString()}")
            val songMetaData = metaDataReader.getMetaDataFromUri(uri)
            if (songMetaData != null) {
                _selectedSongFileName.value = songMetaData.fileName ?: "Unknown"

                selectedSongFileName.value?.let { insertSong(it, uri.toString()) }
            }
        }


    }

    private fun insertSong(title: String, uri: String) {
        viewModelScope.launch {
            musicRepository.insertSong(title, uri)
        }

    }

    private fun fetchSongs() {
        viewModelScope.launch {
            musicRepository.getAllSongs().catch { exception -> println(exception) }.collect {
                _listSong.value = if (it.isEmpty()) emptyList() else it
            }

        }
    }
}

