import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.musicplayer.core.shared.viewmodel.SharedViewModel
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.home.HomeScreen
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.nowplaying.PlayerScreen
import com.dev.musicplayer.presentation.nowplaying.PlayerViewModel
import com.dev.musicplayer.presentation.playlist.PlaylistScreen

@UnstableApi
@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    val musicPlaybackUiState = sharedViewModel.musicPlaybackUiState

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {

        composable(
            route = Screen.HomeScreen.route
        ) {

            val homeViewModel = hiltViewModel<HomeViewModel>()
            val songs by homeViewModel.listSong.collectAsState(initial = emptyList())

            HomeScreen(
                songs = songs,
                onEvent = homeViewModel::onEvent,
                homeUiState = homeViewModel.homeUiState,
                musicPlaybackUiState = musicPlaybackUiState,
                onNavigateToMusicPlayer = {
                    navController.navigate(Screen.PlayerScreen.route)
                },
                selectMusicFromStorage = {
                    homeViewModel.selectMusicFromStorage(it)
                }
            )
        }

        composable(
            route = Screen.PlaylistScreen.route
        ) {
            PlaylistScreen()
        }

        composable(
            route = Screen.PlayerScreen.route,
            enterTransition = {
                expandVertically(
                    animationSpec = tween(400),
                    expandFrom = Alignment.Top,
                    clip = true
                )
            },
            exitTransition = {
                shrinkVertically(
                    animationSpec = tween(400),
                    shrinkTowards = Alignment.Top
                )
            }
        ) {
            val playerViewModel =  hiltViewModel<PlayerViewModel>()

            PlayerScreen(
                onEvent = playerViewModel::onEvent,
                musicPlaybackUiState = musicPlaybackUiState,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }

}

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)