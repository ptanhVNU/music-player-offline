
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.import.ImportScreen
import com.dev.musicplayer.presentation.playlist.PlaylistSCreen
import com.dev.musicplayer.presentation.songs.SongsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.SongsScreen.route,
    ) {
        composable(
            route = Screen.SongsScreen.route
        ) {
            SongsScreen()
        }

        composable(
            route = Screen.PlaylistScreen.route
        ) {
            PlaylistSCreen()
        }

        composable(
            route = Screen.ImportScreen.route
        ) {
            ImportScreen()
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)