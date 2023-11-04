package com.dev.musicplayer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private  val homeViewModel : HomeViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.fetchSongs()
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {

                ChangeSystemBarsTheme(!isSystemInDarkTheme())
                Surface(
                ) {
                    MainApp()
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
}




