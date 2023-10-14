package com.dev.musicplayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.dev.musicplayer.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            MusicPlayerTheme {
                // change the system bar (nav bar and status bar) follow light/dark theme
                ChangeSystemBarsTheme(!isSystemInDarkTheme())

                Surface(
                    modifier = Modifier
                        .windowInsetsPadding(
                            WindowInsets.statusBars
                        ),
                ) {
                    MainScreen()
                }


            }
        }
    }
    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val barColor = MaterialTheme.colorScheme.background.toArgb()
        val navBarColor = Color.Transparent.toArgb()
        LaunchedEffect(lightTheme) {
            if (lightTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        barColor, barColor,
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        navBarColor, barColor,
                    ),
                )
            } else {
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
    }

}




