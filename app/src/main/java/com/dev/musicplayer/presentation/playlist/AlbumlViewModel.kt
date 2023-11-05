package com.dev.musicplayer.presentation.playlist
import androidx.lifecycle.ViewModel
import com.dev.musicplayer.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Album(
    val ID: Int,
    val imageResource: Int,
    val albumName: String,
    val artistName: String,
    val songCount: Int
)

class AlbumViewModel : ViewModel() {
    private val _albumState = MutableStateFlow(emptyList<Album>())
    val albumState: StateFlow<List<Album>> = _albumState.asStateFlow()

    init {
        _albumState.update {
            sampleAlbum()
        }
    }

    fun refresh() {
        _albumState.update {
            sampleAlbum()
        }
    }

    fun removeItem(currentItem: Album) {
        _albumState.update {
            val mutableList = it.toMutableList()
            mutableList.remove(currentItem)
            mutableList
        }
    }

    private fun sampleAlbum(): List<Album> {
        val albums = listOf(
            Album(ID = 1, imageResource = R.drawable.meme, albumName = "Album 1", artistName = "Artist", songCount = 10),
            Album(ID = 2,imageResource = R.drawable.meme, albumName = "Album 2", artistName = "Artist", songCount = 15),
            Album(ID = 3,imageResource = R.drawable.meme, albumName = "Album 3", artistName = "Artist", songCount = 20),
            Album(ID = 4,imageResource = R.drawable.meme, albumName = "Album 4", artistName = "Artist", songCount = 25),
            Album(ID = 5,imageResource = R.drawable.meme, albumName = "Album 5", artistName = "Artist", songCount = 30),
            Album(ID = 6,imageResource = R.drawable.meme, albumName = "Album 6", artistName = "Artist", songCount = 30),
            Album(ID = 7,imageResource = R.drawable.meme, albumName = "Album 7", artistName = "Artist", songCount = 30)
        )
        return albums
    }
}