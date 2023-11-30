package com.dev.musicplayer.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()
    val searchType by viewModel.searchType.collectAsStateWithLifecycle()

    val showGrid by remember {
        derivedStateOf {
//                        ((searchType == SearchType.Songs) || (searchType == SearchType.Albums))
            searchType == SearchType.Songs
        }
    }
    androidx.compose.material3.Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = viewModel::updateQuery,
                onBackArrowPressed = navController::popBackStack,
                currentType = searchType,
                onSearchTypeSelect = viewModel::updateType,
                onClearRequest = viewModel::clearQueryText
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
                    )
            ) {
                if (searchResult.errorMsg != null) {
//                                FullScreenSadMessage(searchResult.errorMsg)
                } else {
                    ResultContent(
                        searchResult = searchResult,
                        showGrid = showGrid,
                        searchType = searchType,
                        onSongClicked = viewModel::handleClick,
//                                        onAlbumClicked = this@SearchFragment::handleClick,
//                                        onArtistClicked = this@SearchFragment::handleClick,
//                                        onAlbumArtistClicked = this@SearchFragment::handleClick,
//                                        onComposerClicked = this@SearchFragment::handleClick,
//                                        onLyricistClicked = this@SearchFragment::handleClick,
//                                        onGenreClicked = this@SearchFragment::handleClick,
                        onPlaylistClicked = viewModel::handleClick,
                    )
                }
            }
        }
    )
}