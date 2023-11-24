package com.dev.musicplayer.presentation.utils

import BottomNavItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.onSecondary

@Composable
fun BottomBar(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?,
    bottomBarState: Boolean,
) {

    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Home",
            route = Screen.HomeScreen.route,
            selectedIcon = Icons.Filled.MusicNote,
            unselectedIcon = Icons.Outlined.MusicNote,
        ),
        BottomNavItem(
            title = "Playlist",
            route = Screen.PlaylistScreen.route,
            selectedIcon = Icons.Filled.LibraryMusic,
            unselectedIcon = Icons.Outlined.LibraryMusic,
        ),
    )

    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            NavigationBar(
                modifier = Modifier.height(95.dp),
                containerColor = Color.Transparent
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
    )
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
                modifier = Modifier.size(25.dp),
                imageVector = if (isSelected) item.selectedIcon else
                    item.unselectedIcon,
                contentDescription = item.title,
            )
        },

        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MusicAppColorScheme.secondary,
            indicatorColor = onSecondary,
        )

    )
}