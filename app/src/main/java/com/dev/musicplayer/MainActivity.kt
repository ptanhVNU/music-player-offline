package com.dev.musicplayer

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.dev.musicplayer.core.services.MusicPlaybackService
import com.dev.musicplayer.core.shared.viewmodel.SharedViewModel
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
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
                    RequestPermissionAndDisplayContent {
                        MainApp(sharedViewModel)
                    }
                }
            }
        }
    }

    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val barColor = Color.Transparent.toArgb()
        val navBarColor = Color.Transparent.toArgb()
        LaunchedEffect(!lightTheme) {
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionAndDisplayContent(
    appContent: @Composable () -> Unit,
) {

    val readAudioPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            Manifest.permission.READ_MEDIA_AUDIO
        )
    } else {
        rememberPermissionState(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    fun requestPermissions(){
        readAudioPermissionState.launchPermissionRequest()
    }

    LaunchedEffect(key1 = Unit){
        if(!readAudioPermissionState.status.isGranted){
            requestPermissions()
        }
    }

    if (readAudioPermissionState.status.isGranted) {

        appContent()

    } else {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                Icons.Rounded.Error,
                null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Grant permission first to use this app",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            if(readAudioPermissionState.status.shouldShowRationale){
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedButton(
                    onClick = { requestPermissions() },
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                       text = "Retry",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}