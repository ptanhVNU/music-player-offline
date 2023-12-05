import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.dev.musicplayer.core.shared.viewmodel.SharedViewModel
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.home.HomeScreen
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.nowplaying.PlayerScreen
import com.dev.musicplayer.presentation.nowplaying.PlayerViewModel
import com.dev.musicplayer.presentation.playlist.AlbumViewModel
import com.dev.musicplayer.presentation.playlist.PlaylistScreen
import com.dev.musicplayer.presentation.playlist.listSongOfAlbum.ListSongScreen
import com.dev.musicplayer.presentation.search.SearchScreen
import com.dev.musicplayer.presentation.search.SearchViewModel


@OptIn(ExperimentalMaterialApi::class, ExperimentalCoilApi::class)

@UnstableApi
@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    val musicPlaybackUiState = sharedViewModel.musicPlaybackUiState
    val homeViewModel = hiltViewModel<HomeViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {

        composable(
            route = Screen.HomeScreen.route
        ) {


//            val refreshing by homeViewModel
            val pullRefreshState = rememberPullRefreshState(
                refreshing = homeViewModel.homeUiState.loading ?: false,
                onRefresh = homeViewModel::getMusicData
            )

            HomeScreen(
                onEvent = homeViewModel::onEvent,
                homeUiState = homeViewModel.homeUiState,
                musicPlaybackUiState = musicPlaybackUiState,
                onNavigateToMusicPlayer = {
                    navController.navigate(Screen.PlayerScreen.route)
                },
                pullRefreshState = pullRefreshState,
                isLoading = homeViewModel.homeUiState.loading ?: false
            )
        }

        composable(
            route = Screen.SearchScreen.route
        ) {
            val searchViewModel = hiltViewModel<SearchViewModel>()

            SearchScreen(
                // search view model on event
                onEvent = searchViewModel::onEvent,
                musicPlaybackUiState = musicPlaybackUiState,
                viewModel = searchViewModel,
                onNavigateToMusicPlayer = {
                    navController.navigate(Screen.PlayerScreen.route)
                },
            )
        }

        composable(
            route = "listSong/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.LongType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getLong("albumId") ?: 0
            val viewModel = hiltViewModel<AlbumViewModel>()
            ListSongScreen(navController, albumId, viewModel)
        }

        composable(
            route = Screen.PlaylistScreen.route
        ) {
            val viewModel = hiltViewModel<AlbumViewModel>()
            val playlist by viewModel.playlist.collectAsState(initial = emptyList())
            PlaylistScreen(
                playlist = playlist,
                onEvent = homeViewModel::onEvent,
                playlistUiState = viewModel.playlistUiState,
                albumViewModel = viewModel,
                navController = navController,
                musicPlaybackUiState = musicPlaybackUiState,
                onNavigateToMusicPlayer = {
                    navController.navigate(Screen.PlayerScreen.route)
                },
            )
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
            val playerViewModel = hiltViewModel<PlayerViewModel>()

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