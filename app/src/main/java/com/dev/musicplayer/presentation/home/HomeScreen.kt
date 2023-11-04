package com.dev.musicplayer.presentation.home

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
) {
    val context = LocalContext.current
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /// select audio
    val selectAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
        // sử dụng để lấy một h oặc nhiều nội dung từ thiết bị
    ) {
        viewModel.selectMusicFromStorage(it)
        // show toast representation num of tracks added to app
        // TODO: handle event from database if added success else show toast
        if (it.isNotEmpty()) {
            Toast.makeText(context, "${it.size} tracks added", Toast.LENGTH_SHORT).show()
        }
    }

    /// get all songs
    val allSong by viewModel.listSong.collectAsState(initial = listOf())
    println("size all song: ${allSong.size}")

    // scroll controller

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "JetMusic",
                        fontWeight = FontWeight.Bold,
                        style = MusicAppTypography.headlineMedium,
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            selectAudioLauncher.launch("audio/*")

                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add audio"
                        )
                    }
                    IconButton(
                        onClick = {
                            //TODO: Implement search bar
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search music",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MusicAppColorScheme.background)
            )


        }
    ) { paddingValues ->
        val scrollState = rememberLazyListState()
        if (allSong.isEmpty())
            Text(text = "Thêm bài hát đầu tiên vào ứng dụng của bạn ")
        else
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = scrollState,
                contentPadding = PaddingValues(8.dp),
//            verticalArrangement = Arrangement.Center,
            ) {
                items(
                    allSong,
                    key = { it.songId }
                ) { item ->
                    SongItem(
                        item = item,
                        modifier = Modifier.fillParentMaxWidth(),
                        onItemClicked = {

                        }
                    )
//                Text(text = data.title)
                }

                //TODO: Implement UI item songs
            }
    }
}
// khi  bấm vào play nhạc => cần trả về uri



