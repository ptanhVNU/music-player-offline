package com.dev.musicplayer.presentation.home

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.musicplayer.core.services.MetaDataReader
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.repositories.MusicRepositoryImpl
import com.dev.musicplayer.domain.use_case.AddMediaItemsUseCase
import com.dev.musicplayer.domain.use_case.GetMusicsUseCase
import com.dev.musicplayer.domain.use_case.PauseMusicUseCase
import com.dev.musicplayer.domain.use_case.PlayMusicUseCase
import com.dev.musicplayer.domain.use_case.ResumeMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMusicsUseCase: GetMusicsUseCase,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val resumeMusicUseCase: ResumeMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
    private val musicRepository: MusicRepositoryImpl,
    private val metaDataReader: MetaDataReader,
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    private val _selectedSongFileName = MutableLiveData<String>()
    private val selectedSongFileName: LiveData<String> get() = _selectedSongFileName

    private var _listSong: MutableStateFlow<List<Song>> = MutableStateFlow(arrayListOf())
    val listSong: StateFlow<List<Song>> = _listSong.asStateFlow()

    init {
        getMusics()

        addMediaItemsUseCase(_listSong.value)
    }


    fun selectMusicFromStorage(uris: List<Uri>) {
        for (uri: Uri in uris) {
            println("uri: ${uri.toString()}")
            val songMetaData = metaDataReader.getMetaDataFromUri(uri)
            if (songMetaData != null) {
                 insertSong(songMetaData.fileName, uri.toString())
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.PlayMusic -> playMusic()

            HomeEvent.ResumeMusic -> resumeMusic()

            HomeEvent.PauseMusic -> pauseMusic()

            is HomeEvent.OnMusicSelected -> {
                homeUiState = homeUiState.copy(selectedMusic = event.selectedMusic)
            }
        }
    }

    private fun insertSong(title: String, uri: String) {
        viewModelScope.launch {
            musicRepository.insertSong(title, uri)

        }
    }

    private fun getMusics() {

        viewModelScope.launch {
            getMusicsUseCase().catch {
                homeUiState = homeUiState.copy(
                    loading = false,
                    errorMessage = it.message
                )
            }.collect {
                Log.d("TAG", "SIZE MUSIC ENTITY: ${it.size}")
                _listSong.value  = it
                homeUiState = homeUiState.copy(
                    loading = false,
                    musics = _listSong.value
                )

                addMediaItemsUseCase(_listSong.value)
            }

        }
    }

    private fun playMusic() {

        homeUiState.apply {
            _listSong.value.indexOf(selectedMusic).let {
                playMusicUseCase(it)
            }
        }
    }

    private fun resumeMusic() {
        resumeMusicUseCase()
    }

    private fun pauseMusic() {
        pauseMusicUseCase()
    }
}

