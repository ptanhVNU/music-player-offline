package com.dev.musicplayer.core.services

import android.app.Application


class AppContainer(private val context: Application){
	val localMediaProvider by lazy {
		LocalMediaProvider(applicationContext = context)
	}
}