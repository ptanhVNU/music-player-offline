package com.dev.musicplayer.presentation.songs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun SongsScreen() {
    Scaffold(
        topBar = {
            Surface (shadowElevation = 3.dp) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Songs",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
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