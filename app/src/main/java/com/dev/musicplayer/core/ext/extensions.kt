package com.dev.musicplayer.core.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64.DEFAULT
import java.nio.ByteBuffer

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


/// using for save and export image
fun bitmapToBase64(bitmap: Bitmap?): String {
    if (bitmap == null) return  ""

    // create a ByteBuffer and allocate size equal to bytes in the bitmap
    val byteBuffer = ByteBuffer.allocate(bitmap.height * bitmap.rowBytes)
    //copy all the pixels from bitmap to byteBuffer
    bitmap.copyPixelsToBuffer(byteBuffer)
    //convert byte buffer into byteArray
    val byteArray = byteBuffer.array()
    //convert byteArray to Base64 String with default flags
    return Base64.encodeToString(byteArray, DEFAULT)
}

fun base64ToBitmap(base64String: String): Bitmap {
    //convert Base64 String into byteArray
    val byteArray = Base64.decode(base64String, DEFAULT)
    //byteArray to Bitmap
    return BitmapFactory.decodeByteArray(
        byteArray,
        0, byteArray.size
    )
}
