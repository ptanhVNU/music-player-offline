package com.dev.musicplayer.presentation.home

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.core.shared.components.MusicPlaybackUiState
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.presentation.home.components.MusicMiniPlayerCard
import com.dev.musicplayer.presentation.home.components.SongItem
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import com.dev.musicplayer.utils.PlayerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    songs: List<Song>,
    onEvent: (HomeEvent) -> Unit,
    homeUiState: HomeUiState,
    musicPlaybackUiState: MusicPlaybackUiState,
    onNavigateToMusicPlayer: () -> Unit,
    selectMusicFromStorage: (List<Uri>) -> Unit,
    onDeleteMusic: (Song) -> Unit,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp


    val selectAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) {
        selectMusicFromStorage(it)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "JetMusic",
                        fontWeight = FontWeight.Bold,
                        style = MusicAppTypography.headlineMedium,
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            selectAudioLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageAndVideo
                                )
                            )
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add audio"
                        )
                    }
                    IconButton(
                        onClick = {
                            //TODO: Implement search bar
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search music",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MusicAppColorScheme.background)
            )
        }
    ) {
        val scrollState = rememberLazyListState()

        with(homeUiState) {
            when (loading) {
                true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                false -> {

                    Box {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            contentPadding = PaddingValues(bottom = 80.dp),
                            state = scrollState,
                        ) {
                            items(
                                songs,
                            ) { item ->
                                SongItem(
                                    item = item,
                                    modifier = Modifier.fillParentMaxWidth(),
                                    onItemClicked = {
                                        Log.d("HOME-SCREEN", "item: ${item.title}")
                                        onEvent(HomeEvent.OnMusicSelected(item))
                                        onEvent(HomeEvent.PlayMusic)
                                    },
                                    onDeleteSong = {
                                        onDeleteMusic(it)
                                    }
                                )
                            }
                        }

                        with(musicPlaybackUiState) {
                            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED) {
                                MusicMiniPlayerCard(
                                    /// TODO: Impl progress bar
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .offset(y = screenHeight - 100.dp),
                                    music = currentMusic,
                                    playerState = playerState,
                                    onResumeClicked = { onEvent(HomeEvent.ResumeMusic) },
                                    onPauseClicked = { onEvent(HomeEvent.PauseMusic) },
                                    onClick = { onNavigateToMusicPlayer() }
                                )
                            }
                        }
                    }
                }

                else -> {}
            }


        }
    }
}




fun convertTemporaryUriToPermanent(context: Context, uri: Uri): Uri {
    return if (DocumentsContract.isDocumentUri(context, uri)) {
        // Nếu là Uri của DocumentProvider, thực hiện xử lý tương ứng
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                // External Storage
                Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + split[1])
            } else {
                // TODO: Xử lý các loại DocumentProvider khác (nếu có)
                uri
            }
        } else if (isDownloadsDocument(uri)) {
            // DownloadsProvider
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                id.toLong()
            )
            Uri.parse("file://" + getDataColumn(context, contentUri, null, null))
        } else if (isMediaDocument(uri)) {
            // MediaProvider
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            when (type) {
                "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            Uri.parse("file://" + getDataColumn(context, contentUri, "_id=?", arrayOf(split[1])))
        } else {
            // TODO: Xử lý các loại DocumentProvider khác (nếu có)
            uri
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        // Uri có scheme là "content"
        Uri.parse("file://" + getDataColumn(context, uri, null, null))
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        // Uri có scheme là "file"
        uri
    } else {
        // Không xác định được loại Uri, trả về nguyên bản
        uri
    }
}

private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

private fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = uri?.let {
            context.contentResolver.query(
                it,
                projection,
                selection,
                selectionArgs,
                null
            )
        }
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(column)
                return it.getString(columnIndex)
            }
        }
    } finally {
        cursor?.close()
    }

    return null
}