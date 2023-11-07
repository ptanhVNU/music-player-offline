package com.dev.musicplayer.presentation.playlist

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.dev.musicplayer.R

@Composable
fun ReturnButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Return"
        )
    }
}

@Composable
fun StartButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(50.dp).background(Color.White, shape = CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Start"
        )
    }
}

@Composable
fun SettingButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Setting"
        )
    }
}

@Preview
@Composable
fun ListSongScreen() {
    var activeReturn by remember {
        mutableStateOf(false)
    }
    var activeStart by remember {
        mutableStateOf(false)
    }
    val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.meme)
    val palette = Palette.from(bitmap).generate()
    val swatch = palette.vibrantSwatch
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        swatch?.let {
            val backgroundColor = it.rgb
            val alpha = 0.8f
            Box(
                modifier = Modifier
                    .background(Color(backgroundColor).copy(alpha = alpha))
                    .height(450.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        ReturnButton(
                            icon = Icons.Default.ArrowBackIos,
                            onClick = {
                                activeReturn = true
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painterResource(id = R.drawable.meme),
                            contentDescription = "Meme",
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally) // Center the image horizontally
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(30.dp))
                            Text(
                                text = "Album1",
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
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}

