package com.dev.musicplayer.data.local.reposity

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicRepository @Inject constructor(@ApplicationContext private  val context: Context) {
}