package com.dev.musicplayer

import NavGraph
import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.dev.musicplayer.core.shared.viewmodel.SharedViewModel
import com.dev.musicplayer.presentation.utils.BottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@UnstableApi
fun MainApp(
    sharedViewModel: SharedViewModel,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
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

