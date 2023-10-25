package com.dev.musicplayer.presentation.utils

import BottomNavItem
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dev.musicplayer.navigation.Screen

@Composable
fun BottomBar(navController: NavHostController) {

    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Home",
            route = Screen.SongsScreen.route,
            selectedIcon = Icons.Filled.MusicNote,
            unselectedIcon = Icons.Outlined.MusicNote,
        ),
        BottomNavItem(
            title = "Playlist",
            route = Screen.PlaylistScreen.route,
            selectedIcon = Icons.Filled.LibraryMusic,
            unselectedIcon = Icons.Outlined.LibraryMusic,
        ),
        BottomNavItem(
            title = "Import",
            route = Screen.ImportScreen.route,
            selectedIcon = Icons.Filled.Download,
            unselectedIcon = Icons.Outlined.Download,
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier.height(105.dp),
    ) {
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

        onClick = {
            navController.navigate(item.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },

        icon = {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = if (isSelected) item.selectedIcon else
                    item.unselectedIcon,
                contentDescription = item.title,
            )
        },

        )
}