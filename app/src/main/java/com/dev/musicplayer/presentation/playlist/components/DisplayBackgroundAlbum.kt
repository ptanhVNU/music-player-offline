package com.dev.musicplayer.presentation.playlist.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.dev.musicplayer.core.ext.loadBitmapFromUrl
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun DisplayBackgroundAlbum(
    onBackButtonClicked: ()-> Unit,
    playlist: Playlist,
    setShowSettingSheet: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val url = "https://i1.sndcdn.com/artworks-y4ek09OJcvON38Ys-gs2icQ-t500x500.jpg"
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

    bitmap.value?.let { loadedBitmap ->
        val palette = Palette.from(loadedBitmap).generate()
        val swatch = palette.vibrantSwatch

        swatch?.let {
            val imageBackgroundColor = it.rgb

            val gradientColors = listOf(
                Color(imageBackgroundColor).copy(0.8f),
                Color(imageBackgroundColor).copy(alpha = 0.6f),
                Color(imageBackgroundColor).copy(alpha = 0.3f),
//                MusicAppColorScheme.background.copy(alpha = 0.5f),
                MusicAppColorScheme.background.copy(alpha = 0.7f),
//                MusicAppColorScheme.background.copy(alpha = 0.9f),
            )

            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = gradientColors,
                            startY = 0f,
                            endY = 800f, // Adjust the endY value as needed for the gradient length
                        )
                    )
                    .height(350.dp)
                    .padding(
                        start = 10.dp, top = 5.dp,
                    )
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BackButton(
                            icon = Icons.Default.ArrowBackIos,
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.CenterVertically),
                            onClick = onBackButtonClicked,
                        )

                        SettingButton(
                            icon = Icons.Default.MoreHoriz,
                            onClick = {
                                setShowSettingSheet(true)
                            },
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    AsyncImage(
                        modifier = Modifier
                            .size(220.dp)
                            .align(Alignment.CenterHorizontally),
                        model = url,
                        contentDescription = "Title Album"
                    )

                    Text(
                        text = playlist.title,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 20.dp),
                        style = MusicAppTypography.titleMedium.copy(
                            color = MusicAppColorScheme.onBackground,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }

            }
        }
    }
}

