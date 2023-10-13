package com.dev.musicplayer

import BottomNavGraph
import BottomNavItem
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.musicplayer.navigation.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
//    val screen = listOf<Screen>(
//        Screen.SongsScreen,
//        Screen.PlaylistScreen,
//        Screen.ImportScreen,
//    )

    val bottomNavItems = listOf<BottomNavItem>(
        BottomNavItem(
            title = "Songs",
            route = Screen.SongsScreen.route,
            selectedIcon = Icons.Filled.MusicNote,
            unselectedIcon = Icons.Outlined.MusicNote,
        ),
        BottomNavItem(
            title = "Playlist",
            route = Screen.PlaylistScreen.route,
            selectedIcon = Icons.Filled.PlaylistPlay,
            unselectedIcon = Icons.Outlined.PlaylistPlay,
        ),
        BottomNavItem(
            title = "Import",
            route = Screen.ImportScreen.route,
            selectedIcon = Icons.Filled.DownloadForOffline,
            unselectedIcon = Icons.Outlined.DownloadForOffline,
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination;

    NavigationBar {
        bottomNavItems.forEach { item ->
            AddItem(
                item = item,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    item: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == item.route
    } == true

    NavigationBarItem(
        selected = isSelected,
        label = {
            Text(text = item.title)
        },
        onClick = {
            navController.navigate(item.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else
                    item.unselectedIcon,
                contentDescription = item.title,
            )
        },
    )
}