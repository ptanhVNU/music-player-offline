package com.dev.musicplayer.presentation.playlist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.presentation.home.HomeEvent
import com.dev.musicplayer.presentation.playlist.Component.PlaylistItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    playlist: List<Playlist>,
    onEvent: (PlaylistEvent) -> Unit,
    playlistUiState: PlaylistUiState,
    albumViewModel: AlbumViewModel,
    navController : NavController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    var textAdd by remember {
        mutableStateOf("")
    }

    var activeSort by remember {
        mutableStateOf(false)
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return super.onPostFling(consumed, available)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column {
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "   Your Library",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                PlusButton(
                    icon = Icons.Default.Add,
                    onClick = {
                        showBottomSheet = true
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SortButton(
                icon = Icons.Default.Sort,
                onClick = {
                    activeSort = true;
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                val scrollState = rememberLazyListState()
                with(playlistUiState) {
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
                                            .fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 80.dp),
                                    state = scrollState,
                                ) {
                                    itemsIndexed(
                                        items = playlist,
                                        key = { _, item -> item.hashCode() }
                                    ) { _, item ->
                                        PlaylistItem(
                                            item = item,
                                            albumViewModel = albumViewModel,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
            Scaffold(
            ) {
                    contentPadding->Box(
                modifier = Modifier.padding(contentPadding
                )
            )
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextField(
                                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                                value = textAdd,
                                onValueChange = {
                                    textAdd = it
                                },
                                placeholder = {
                                    Text(text = "Name of playlist")
                                },
                                label = {
                                    Text("")
                                }
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Button(
                                onClick = {
                                    albumViewModel.createPlaylist(textAdd)
                                    textAdd = ""
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                }
                            ) {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
    }
}


