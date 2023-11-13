package com.dev.musicplayer.presentation.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.dev.musicplayer.R
import com.dev.musicplayer.data.local.entities.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


@Composable
fun ListSongScreen(navController: NavController, albumID:Long, viewModel: AlbumViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val album = viewModel.getAlbumById(albumID)
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        rvBackGroundAlbum(navController, album!!, context)
    }
}


suspend fun loadBitmapFromUrl(url: String, context: Context): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun rvBackGroundAlbum(navController: NavController, album: Playlist, context: Context) {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var activeReturn by remember {
        mutableStateOf(false)
    }
    var activeStart by remember {
        mutableStateOf(false)
    }
    val url = album.thumbnail ?: ""
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(url) {
        val job = GlobalScope.launch(Dispatchers.IO) {
            val loadedBitmap = loadBitmapFromUrl(url, context)
            withContext(Dispatchers.Main) {
                bitmap.value = loadedBitmap
            }
        }

        onDispose {
            job.cancel()
        }
    }

    Box() {
        bitmap.value?.let { loadedBitmap ->
            val palette = Palette.from(loadedBitmap).generate()
            val swatch = palette.vibrantSwatch

            swatch?.let {
                val backgroundColor = it.rgb
                val alpha = 0.8f
                Box(
                    modifier = Modifier
                        .background(Color(backgroundColor).copy(alpha = alpha))
                        .height(450.dp)
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(10.dp))
                            ReturnButton(
                                icon = Icons.Default.ArrowBackIos,
                                onClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(200.dp)
                                    .align(Alignment.CenterHorizontally),
                                model = album.thumbnail,
                                contentDescription = "Title Album"
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(30.dp))
                                Text(
                                    text = album.title,
                                    color = Color.White,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(30.dp))
                                Text(
                                    text = "Danh sách phát của bạn",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row {
                                    Spacer(modifier = Modifier.width(30.dp))
                                    StartButton(
                                        icon = Icons.Default.PlayArrow,
                                        onClick = {
                                            activeStart = true
                                        }
                                    )
                                }
                                SettingButton(
                                    icon = Icons.Default.MoreVert,
                                    onClick = {
                                        showBottomSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    //chua fix duoc cai nay
//            Scaffold() {
//            contentPadding->Box(
//            modifier = Modifier.padding(contentPadding)
//            )
//            if (showBottomSheet) {
//                ModalBottomSheet(
//                    onDismissRequest = {
//                        showBottomSheet = false
//                    },
//                    sheetState = sheetState
//                ) {
//                    Column (
//                        modifier = Modifier.fillMaxWidth().padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        EditTitleButton(
//                            icon = Icons.Default.Edit,
//                            onClick = {}
//                        )
//
//                        EditImageButton(
//                            icon = Icons.Default.Image,
//                            onClick = {}
//                        )
//
//                        RemoveAlbumButton(
//                            icon = Icons.Default.Delete,
//                            onClick = {}
//                        )
//
//                        Button(
//                            onClick = {
//                                scope.launch { sheetState.hide() }.invokeOnCompletion {
//                                    if (!sheetState.isVisible) {
//                                        showBottomSheet = false
//                                    }
//                                }
//                            }
//                        ) {
//                            Text("Submit")
//                        }
//                    }
//                }
//            }
//        }
}
