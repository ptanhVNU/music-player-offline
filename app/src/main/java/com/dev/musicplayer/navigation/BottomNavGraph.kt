
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.home.HomeScreen
import com.dev.musicplayer.presentation.home.HomeViewModel
import com.dev.musicplayer.presentation.playlist.PlaylistScreen

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
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(viewModel)
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