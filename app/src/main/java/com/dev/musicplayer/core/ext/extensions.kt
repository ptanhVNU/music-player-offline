package com.dev.musicplayer.core.ext

import android.app.ActivityManager
import android.app.Service
import android.content.Context

fun Context.isMyServiceRunning(serviceClass: Class<out Service>) = try {
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Int.MAX_VALUE)
        .any { it.service.className == serviceClass.name }
} catch (e: Exception) {
    false
}

fun Long.toTime(): String {
    val stringBuffer = StringBuffer()

    val minutes = (this / 60000).toInt()
    val seconds = (this % 60000 / 1000).toInt()

    stringBuffer
        .append(String.format("%02d", minutes))
        .append(":")
        .append(String.format("%02d", seconds))

    return stringBuffer.toString()
}

fun String.removeFileExtension(): String {
    val lastDotIndex = lastIndexOf('.')
    return if (lastDotIndex != -1) {
        substring(0, lastDotIndex)
    } else {
        this
    }
}