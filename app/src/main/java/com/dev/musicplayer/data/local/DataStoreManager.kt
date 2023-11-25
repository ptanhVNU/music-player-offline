package com.dev.musicplayer.data.local

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateListOf
import com.dev.musicplayer.MusicApplication
import com.dev.musicplayer.data.local.components.DaoCollection
import com.dev.musicplayer.data.local.components.QuerySearch
import com.dev.musicplayer.data.local.entities.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val context: Context,
    private val daoCollection: DaoCollection,
    private val scope: CoroutineScope,
) {
    val querySearch by lazy { QuerySearch(daoCollection) }

    private var callback: Callback? = null

    private val _queue = mutableStateListOf<Song>()
    val queue: List<Song> = _queue

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    private var remIdx = 0

    @Synchronized
    fun setQueue(newQueue: List<Song>, startPlayingFromIndex: Int) {
        if (newQueue.isEmpty()) return
        _queue.apply {
            clear()
            addAll(newQueue)
        }
        _currentSong.value = newQueue[startPlayingFromIndex]
        if (callback == null) {
            val intent = Intent(context, MusicApplication::class.java)
            context.startForegroundService(intent)
            remIdx = startPlayingFromIndex
        } else {
            callback?.setQueue(newQueue, startPlayingFromIndex)
        }
    }

    interface Callback {
        fun setQueue(newQueue: List<Song>, startPlayingFromIndex: Int)
        fun addToQueue(song: Song)
        fun updateNotification()
    }
}