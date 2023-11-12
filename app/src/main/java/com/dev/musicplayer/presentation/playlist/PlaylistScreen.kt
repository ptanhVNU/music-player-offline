package com.dev.musicplayer.presentation.playlist

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PlaylistScreen() {
<<<<<<< HEAD
    Scaffold(
        topBar = {
            Surface(shadowElevation = 5.dp) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Playlist",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                //TODO: Implement search bar
                            },
                        ) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search music")
                        }
=======
    var text by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }
    var items = remember {
        mutableStateListOf(
            "Kimetsu no Yaiba",
            "Jujutsu Kaisen",
            "Noragami",
            "Bleach"
        )
    }
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
    val albumViewModel = viewModel<AlbumViewModel>()
    val albumsState by albumViewModel.albumState.collectAsState()
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
>>>>>>> main
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    query = text,
                    onQueryChange = {
                        text = it
                    },
                    onSearch = {
                        items.add(text)
                        active = false
                    },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = {
                        Text(text = "Search")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {
                        if(active) {
                            Icon(
                                modifier = Modifier.clickable {
                                    if(text.isNotEmpty()) {
                                        text = ""
                                    } else {
                                        //ấn close thì list search biến mất
                                        active = false;
                                    }
                                },
                                imageVector =  Icons.Default.Close,
                                contentDescription = "Close Icon"
                            )
                        }
                    }
                ) {
                    items.forEach {
                        Row(
                            modifier = Modifier.padding(all = 14.dp)
                        ) {
                            Icon(
                                modifier = Modifier.padding(end = 10.dp),
                                imageVector = Icons.Default.History,
                                contentDescription = "History Icon"
                            )
                            Text(
                                text = it
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SortButton(
                icon = Icons.Default.Sort,
                onClick = {
                    activeSort = true;
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.nestedScroll(nestedScrollConnection)
            ) {
                itemsIndexed(
                    items = albumsState,
                    key = { _, item -> item.hashCode() }
                ) { _, albumContent ->
                    albumItem(albumContent, onRemove = albumViewModel::removeItem)
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
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
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

