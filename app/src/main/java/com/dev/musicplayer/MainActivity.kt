package com.dev.musicplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.media3.common.util.UnstableApi
import com.dev.musicplayer.core.services.MusicPlaybackService
import com.dev.musicplayer.core.shared.viewmodel.SharedViewModel
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@UnstableApi
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MusicAppTheme {
                ChangeSystemBarsTheme(!isSystemInDarkTheme())

                Surface() {
                    MainApp(sharedViewModel)
                }
            }
        }
    }

    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val barColor = MusicAppColorScheme.background.toArgb()
        val navBarColor = Color.Transparent.toArgb()
        LaunchedEffect(lightTheme) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(
                    barColor,
                ),
                navigationBarStyle = SystemBarStyle.dark(
                    navBarColor,
                ),
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        sharedViewModel.destroyMediaController()
        stopService(Intent(this, MusicPlaybackService::class.java))
    }
}