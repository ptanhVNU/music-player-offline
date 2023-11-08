import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.musicplayer.core.shared.viewmodel.AudioViewModel
import com.dev.musicplayer.core.shared.viewmodel.UIEvents
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.home.HomeScreen
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.playlist.PlaylistScreen

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
            HomeScreen(
                homeViewModel = homeViewModel,
//                audioViewModel = audioViewModel,
//                progress = audioViewModel.progress,
//                onProgress = { audioViewModel.onUiEvents(UIEvents.SeekTo(it)) },
//                isAudioPlaying = audioViewModel.isPlaying,
                songs = songs,
//                currentPlayingAudio = audioViewModel.currentSelectedAudio,
//                onStart = {
//                    audioViewModel.onUiEvents(UIEvents.PlayPause)
//                },
                onItemClick = {
                    audioViewModel.onUiEvents(UIEvents.SelectedAudioChange(it))
                    print("index: $it")
                },
//                onNext = {
//                    audioViewModel.onUiEvents(UIEvents.SeekToNext)
//                }
            )
        }

        composable(
            route = Screen.PlaylistScreen.route
        ) {
            PlaylistScreen()
        }

//        composable(
//            route = Screen.SettingScreen.route

//        ) {
//            SettingScreen()
//        }
    }

}

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)