package com.dev.musicplayer.presentation.nowplaying



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.Duration
import java.util.*
import  androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import com.dev.musicplayer.R

@Composable
fun TopPlayerScreenBar() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 30.dp),
        ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Arrow",
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.PlaylistAdd,
                contentDescription = "Add list",
                tint = Color.White,
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.White,
            )
        }
    }
}

@Composable
fun SongDescription(title: String, name: String){
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold,
        color = Color.White,
    )
    CompositionLocalProvider(value = LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            color = Color.White,
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlayerSlider(ofHours: Duration?) {
    if (ofHours != null) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.like_button) ,
                contentDescription = "Like Button",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(30.dp).offset(x = 300.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Slider(value = 0f,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTickColor = Color.White,)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "0:00", color = Color.White, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${ofHours}:00", color = Color.White, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PlayerButtons(
    modifier: Modifier = Modifier,
    playerButtonSize: Dp =  72.dp,
    sideButtonSize: Dp =  42.dp,
    smallSideButtonSize: Dp = 32.dp,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        val buttonModifier = Modifier
            .semantics {
                role = Role.Button
            }
        Image(
            painter = painterResource(id = R.drawable.shuffle),
            contentDescription = "Shuffle",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier
                .size(smallSideButtonSize)
                .offset(y = (5).dp),
        )
        Spacer(modifier = Modifier.size(30.dp))
        Image(imageVector = Icons.Filled.SkipPrevious,
            contentDescription = "Skip Previous",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier.size(sideButtonSize),
        )
        Spacer(modifier = Modifier.size(30.dp))
        Image(
            imageVector = Icons.Filled.PlayCircleFilled,
            contentDescription = "Play",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(playerButtonSize)
                .offset(y = (-10).dp)
                .semantics { role = Role.Button }
        )
        Spacer(modifier = Modifier.size(30.dp))
        Image(imageVector = Icons.Filled.SkipNext,
            contentDescription = "Skip Next",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier.size(sideButtonSize),
        )
        Spacer(modifier = Modifier.size(30.dp))
        Image(
            painter = painterResource(id = R.drawable.loop),
            contentDescription = "Loop",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier
                .size(smallSideButtonSize)
                .offset(y = (5).dp),
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlayerScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black)
        .padding(horizontal = 10.dp)) {
        TopPlayerScreenBar()
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(30.dp))
            Image(painter = painterResource(
                id = R.drawable.test_player),
                contentDescription = "Song Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .weight(10f)
            )
            Spacer(modifier = Modifier.height(30.dp))
            SongDescription(title = "Title Song", name = "Singer Name")
            Spacer(modifier = Modifier.height(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(10f)) {
                PlayerSlider(ofHours = Duration.ofHours(2))
                Spacer(modifier = Modifier.height(40.dp))
                PlayerButtons(modifier = Modifier.padding(vertical = 8.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}