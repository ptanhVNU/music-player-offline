package com.dev.musicplayer.presentation.songs

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.reposity.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class SongsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val metaDataReader: MetaDataReader,
    private val musicRepository: MusicRepository,
) : ViewModel() {
    val id: MutableLiveData<Long> = MutableLiveData()

    private val _selectedSongFileName = MutableLiveData<String>()
    val selectedSongFileName: LiveData<String> get() = _selectedSongFileName

    private var _songEntity: MutableLiveData<Song> = MutableLiveData()
    val songEntity: LiveData<Song> = _songEntity

    private var _listSong: MutableLiveData<List<Song>> = MutableLiveData()
    val listTrack: LiveData<List<Song>> = _listSong

    val listJob: MutableStateFlow<ArrayList<Song>> = MutableStateFlow(arrayListOf())

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectMusicFromStorage(uris: List<Uri>) {
        for (uri: Uri in uris) {
            val songMetaData = metaDataReader.getMetaDataFromUri(uri)
            if (songMetaData != null) {
                _selectedSongFileName.value = songMetaData.fileName ?: "Unknown"
                _songEntity.value = Song(
                    title = _selectedSongFileName.value!!,
                    createdAt = Instant.now().toEpochMilli()
                )
                insertSong(_songEntity.value!!)
            }
        }


    }

    fun insertSong(song: Song) {
        viewModelScope.launch {
            musicRepository.insertSong(song)
        }
    }

    fun getLocalSongs() {
        viewModelScope.launch {
            musicRepository.getAllSongs().collect {
                _listSong.postValue(it)
            }
        }
    }


}