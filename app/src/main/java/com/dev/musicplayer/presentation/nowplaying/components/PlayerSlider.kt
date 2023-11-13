package com.dev.musicplayer.presentation.nowplaying.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.R
import com.dev.musicplayer.core.ext.toTime
import com.dev.musicplayer.ui.theme.MusicAppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSlider(
    currentPosition: Long,
    totalDuration: Long,
    onValueChanged: (Float) -> Unit
) {

    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.like_button),
            contentDescription = "Like Button",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(30.dp)
                .offset(x = 300.dp)
        )
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Slider(
                value = currentPosition.toFloat(),
                valueRange = 0f..totalDuration.toFloat(),
                onValueChange = {
                    onValueChanged(it)
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTickColor = Color.White,
                )
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = currentPosition.toTime(),
                color = Color.White,
                style = MusicAppTypography.bodySmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = totalDuration.toTime(),
                color = Color.White,
                style = MusicAppTypography.bodySmall
            )
        }
    }
}


