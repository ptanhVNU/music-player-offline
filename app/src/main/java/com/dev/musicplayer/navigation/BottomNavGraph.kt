
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.musicplayer.core.shared.viewmodel.AudioViewModel
import com.dev.musicplayer.core.shared.viewmodel.UIEvents
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.home.HomeScreen
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.playlist.PlaylistScreen

@UnstableApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,

    ) {

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {

            val homeViewModel = hiltViewModel<HomeViewModel>()
            val audioViewModel = hiltViewModel<AudioViewModel>()

            val songs by homeViewModel.listSong.collectAsState(initial = emptyList())
            val progress by audioViewModel.progress.collectAsState(initial = 0F)
            val isPlaying by audioViewModel.isPlaying.collectAsState()



            HomeScreen(
                homeViewModel = homeViewModel,
                audioViewModel = audioViewModel,
                progress = progress,
                onProgress = { audioViewModel.onUiEvents(UIEvents.SeekTo(it)) },
                isAudioPlaying = isPlaying,
                songs = songs,
//                currentPlayingAudio = audioViewModel.,
                onStart = {
                    audioViewModel.onUiEvents(UIEvents.PlayPause)
                },
                onItemClick = {
                    audioViewModel.onUiEvents(UIEvents.SelectedAudioChange(it))
                    Log.d("TAG", "HOME: $it")
                },
                onNext = {
                    audioViewModel.onUiEvents(UIEvents.SeekToNext)
                }
            )
        }

        composable(
            route = Screen.PlaylistScreen.route
        ) {
            PlaylistScreen()
        }
    }

}

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)