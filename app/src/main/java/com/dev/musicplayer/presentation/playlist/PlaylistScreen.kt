package com.dev.musicplayer.presentation.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistSCreen() {
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
                    }
                )
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {

        }
    }
}