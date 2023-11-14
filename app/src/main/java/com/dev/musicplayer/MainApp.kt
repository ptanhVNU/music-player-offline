package com.dev.musicplayer

import NavGraph
import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.musicplayer.core.shared.viewmodel.SharedViewModel
import com.dev.musicplayer.navigation.Screen
import com.dev.musicplayer.presentation.utils.BottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@UnstableApi
fun MainApp(
    sharedViewModel: SharedViewModel,
) {
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        Screen.HomeScreen.route -> {
            bottomBarState.value = true
        }

        Screen.PlaylistScreen.route -> {
            bottomBarState.value = true
        }
        else -> {
            bottomBarState.value = false
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                navBackStackEntry = navBackStackEntry,
                bottomBarState = bottomBarState.value,
            )
        },
    ) {
        NavGraph(
            navController = navController,
            sharedViewModel = sharedViewModel,
        )
    }

//    DisposableEffect(navController) {
//        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
//        }
//
//        navController.addOnDestinationChangedListener(listener)
//
//        onDispose {
//            navController.removeOnDestinationChangedListener(listener)
//        }
//    }
}

