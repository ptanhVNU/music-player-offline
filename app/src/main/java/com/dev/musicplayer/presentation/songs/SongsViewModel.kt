package com.dev.musicplayer.presentation.songs

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dev.musicplayer.services.MetaDataReader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SongsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val metaDataReader: MetaDataReader,
) : ViewModel() {
    private val _selectedSongFileName = MutableLiveData<Uri>()
    val selectedSongFileName: LiveData<Uri> get() = _selectedSongFileName

    fun setSongFileName (fileName:Uri) {
        _selectedSongFileName.value = fileName
    }


}