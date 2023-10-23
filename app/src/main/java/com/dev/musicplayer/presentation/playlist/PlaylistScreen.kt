package com.dev.musicplayer.presentation.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlusButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Plus"
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen() {
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(25.dp))
                Text(
                    text = "Playlist",
                    color = Color.Green,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                PlusButton(
                    icon = Icons.Default.Add,
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
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
    }
}