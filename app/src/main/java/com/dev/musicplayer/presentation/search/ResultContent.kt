package com.dev.musicplayer.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song

@Composable
fun TextCard(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(12.dp),
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun SongCardV3(
    song: Song,
    onSongClicked: (Song) -> Unit,
) = Column(
    modifier = Modifier
        .widthIn(max = 200.dp)
        .fillMaxWidth()
        .clickable(onClick = { onSongClicked(song) })
        .padding(10.dp),
    horizontalAlignment = Alignment.Start,
) {
    AsyncImage(
        model = song.thumbnail,
        contentDescription = "song-album-art",
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium),
        contentScale = ContentScale.Crop,
    )
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )
    Text(
        text = song.title,
        maxLines = 1,
        style = MaterialTheme.typography.titleMedium,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ResultContent(
    searchResult: SearchResult,
    showGrid: Boolean,
    searchType: SearchType,
    onSongClicked: (Song) -> Unit,
//    onAlbumClicked: (Album) -> Unit,
//    onArtistClicked: (Artist) -> Unit,
//    onAlbumArtistClicked: (AlbumArtist) -> Unit,
//    onComposerClicked: (Composer) -> Unit,
//    onLyricistClicked: (Lyricist) -> Unit,
//    onGenreClicked: (Genre) -> Unit,
    onPlaylistClicked: (Playlist) -> Unit,
){
    LazyVerticalGrid(
        columns = if (showGrid) GridCells.Adaptive(150.dp) else GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = WindowInsets.systemBars.only(
            WindowInsetsSides.Bottom).asPaddingValues(),
    ) {
        when(searchType){
            SearchType.Songs -> {
                items(
                    items = searchResult.songs,
                    key = { it.songId }
                ){ song ->
                    SongCardV3(
                        song = song,
                        onSongClicked = onSongClicked,
                    )
                }
            }
//            SearchType.Albums -> {
//                items(
//                    items = searchResult.albums,
//                    key = { it.name }
//                ){ album ->
//                    AlbumCard(
//                        album = album,
//                        onAlbumClicked = onAlbumClicked,
//                    )
//                }
//            }
//            SearchType.Artists -> {
//                items(
//                    items = searchResult.artists,
//                    key = { it.name }
//                ){ artist ->
//                    TextCard(
//                        text = artist.name,
//                        onClick = { onArtistClicked(artist) },
//                    )
//                }
//            }
//            SearchType.AlbumArtists -> {
//                items(
//                    items = searchResult.albumArtists,
//                    key = { it.name }
//                ){ albumArtist ->
//                    TextCard(
//                        text = albumArtist.name,
//                        onClick = { onAlbumArtistClicked(albumArtist) },
//                    )
//                }
//            }
//            SearchType.Lyricists -> {
//                items(
//                    items = searchResult.lyricists,
//                    key = { it.name }
//                ){ lyricist ->
//                    TextCard(
//                        text = lyricist.name,
//                        onClick = { onLyricistClicked(lyricist) },
//                    )
//                }
//            }
//            SearchType.Composers -> {
//                items(
//                    items = searchResult.composers,
//                    key = { it.name }
//                ){ composer ->
//                    TextCard(
//                        text = composer.name,
//                        onClick = { onComposerClicked(composer) },
//                    )
//                }
//            }
//            SearchType.Genres -> {
//                items(
//                    items = searchResult.genres,
//                    key = { it.genre }
//                ){ genre ->
//                    TextCard(
//                        text = genre.genre,
//                        onClick = { onGenreClicked(genre) },
//                    )
//                }
//            }
            SearchType.Playlists -> {
                items(
                    items = searchResult.playlists,
                    key = { it.id }
                ){ playlist ->
                    TextCard(
                        text = playlist.title,
                        onClick = { onPlaylistClicked(playlist) },
                    )
                }
            }
        }
    }
}