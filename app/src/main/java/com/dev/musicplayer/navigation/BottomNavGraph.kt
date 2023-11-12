import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.playlist.AlbumViewModel
import com.dev.musicplayer.presentation.playlist.ListSongScreen
import com.dev.musicplayer.presentation.playlist.PlaylistScreen
import com.dev.musicplayer.presentation.setting.SettingScreen
import com.dev.musicplayer.presentation.songs.SongsScreen
import com.dev.musicplayer.presentation.songs.SongsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.SongsScreen.route,
    ) {
        composable(
            route = Screen.SongsScreen.route
        ) {
            val viewModel = hiltViewModel<SongsViewModel>()
            SongsScreen(viewModel)
        }

        composable(
            route = Screen.PlaylistScreen.route
        ) {
            PlaylistScreen(navController)
        }

        composable(
            route = "listSong/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.LongType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getLong("albumId") ?: 0
            val viewModel =  hiltViewModel<AlbumViewModel>()
            ListSongScreen(navController, albumId, viewModel)
        }

        composable(
            route = Screen.SettingScreen.route
        ) {
            SettingScreen()
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)