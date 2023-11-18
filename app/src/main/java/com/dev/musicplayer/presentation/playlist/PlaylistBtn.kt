package com.dev.musicplayer.presentation.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.ui.theme.MusicAppTypography

@Composable
fun PlusButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Plus"
        )
    }
}

@Composable
fun SortButton(icon: ImageVector, onClick: () -> Unit) {
    Box(

        modifier = Modifier
            .height(25.dp)
            .padding(start = 10.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Sort"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Recently played",
                style = MusicAppTypography.bodyMedium
            )
        }
    }
}

@Composable
fun EditTitleButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = icon,
                contentDescription = "Edit Title Album"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Chỉnh sửa tiêu đề"
            )
        }
    }
}

@Composable
fun EditImageButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = icon,
                contentDescription = "Edit image album"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Chỉnh sửa hình ảnh"
            )
        }
    }
}

@Composable
fun RemoveAlbumButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = icon,
                contentDescription = "Remove album"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Xóa danh sách phát"
            )
        }
    }
}


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
            contentDescription = "Start",
            tint = Color.Black
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