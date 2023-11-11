package com.dev.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.UnstableApi
import com.dev.musicplayer.core.ext.isMyServiceRunning
import com.dev.musicplayer.core.services.MusicService
import com.dev.musicplayer.core.services.MusicServiceHandler
import com.dev.musicplayer.core.shared.viewmodel.AudioViewModel
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@UnstableApi
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<AudioViewModel>()
    private var action: String? = null
    private var data: Uri? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is MusicService.MusicBinder) {
                Log.w("MainActivity", "onServiceConnected .....")
                viewModel.musicServiceHandler = MusicServiceHandler(
                    player = service.service.player,
                    mediaSession = service.service.mediaSession,
                    context = service.service,
                )
                viewModel.init()
                runCollect()
                Log.w("TEST", viewModel.musicServiceHandler?.player.toString())
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.w("MainActivity", "onServiceDisconnected....")
            viewModel.musicServiceHandler = null
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.intent.value = intent
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume ....")
    }

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (viewModel.recreateActivity.value == true) {
            runCollect()
        }
        startMusicService()
        Log.d("MainActivity", "onCreate: ")
        action = intent.action
        data = intent?.data ?: intent?.getStringExtra(Intent.EXTRA_TEXT)?.toUri()
        if (data != null) {
            viewModel.intent.value = intent
        }

        if (!isMyServiceRunning(MusicService::class.java)) {
            // TODO: show miniplayer
        }



        setContent {
            MusicAppTheme {
                ChangeSystemBarsTheme(!isSystemInDarkTheme())

                Surface() {
                    MainApp()
                }
            }
        }
    }

    private fun runCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                val job1 = launch {
                    viewModel.progress.collect {
                        val progress = (it*100)
                            .toInt()                        /// cấp cho miniplayer progress bar
                        /// TODO: thiết kế miniplayer và cho truyền vào progress
                    }
                }

                val job2 = launch {
                    viewModel.isPlaying.collect { //TODO: set miniplayer icon play or pause
                        if (it) {

                        }else {

                        }
                    }
                }
                job1.join()
                job2.join()
            }
        }
    }

    private fun startMusicService() {
        println("go to StartMusicService")

        if (viewModel.recreateActivity.value != true) {
            val intent = Intent(this, MusicService::class.java)
            startService(intent)
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
            viewModel.isServiceRunning.value = true
            Log.d("Service", "Service started")
        }
    }

    private fun stopService(){
        if (viewModel.recreateActivity.value != true){
            viewModel.isServiceRunning.value = false
            viewModel.musicServiceHandler?.release()
            viewModel.musicServiceHandler = null
            unbindService(serviceConnection)
            Log.d("Service", "Service stopped")
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