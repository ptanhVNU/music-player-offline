package com.dev.musicplayer.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.musicplayer.ui.theme.MusicAppColorScheme
import com.dev.musicplayer.ui.theme.MusicAppTypography

@Composable
@Preview
fun SearchBarPreview() {
    SearchBar(
        query = "",
        onQueryChange = {},
        currentType = SearchType.Playlists,
        onSearchTypeSelect = {}
    ) {

    }
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,

    currentType: SearchType,
    onSearchTypeSelect: (SearchType) -> Unit,
    onClearRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MusicAppColorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MusicAppColorScheme.surface,
                unfocusedContainerColor = MusicAppColorScheme.surface,
                disabledContainerColor = MusicAppColorScheme.surface,
                cursorColor = MusicAppColorScheme.secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            value = query,
            onValueChange = onQueryChange,

            trailingIcon = {
                Icon(
                    imageVector = if (query.isEmpty()) Icons.Outlined.Search else Icons.Outlined.Close,
                    contentDescription = null,
                    tint =  MusicAppColorScheme.onSurface,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(9.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = false,
                                radius = 25.dp,
                            ),
                            onClick = onClearRequest
                        ),
                )
            },
            placeholder = {
                Text(
                    text = "Search for songs, playlists, ...",
                    style = MusicAppTypography.titleMedium.copy(color = Color.Gray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            singleLine = true,
            textStyle =  MusicAppTypography.titleMedium,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        )
        SearchTypeSelector(
            currentType = currentType,
            onSearchTypeSelect = onSearchTypeSelect
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTypeSelector(
    currentType: SearchType,
    onSearchTypeSelect: (SearchType) -> Unit,
){
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        items(
            items = SearchType.values(),
            key = { it.name }
        ){ type ->
            FilterChip(
                selected = (type == currentType),
                onClick = { onSearchTypeSelect(type) },
                label = {
                    Text(
                        text = type.text,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }
    }
}