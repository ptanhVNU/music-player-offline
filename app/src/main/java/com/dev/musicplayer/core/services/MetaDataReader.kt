package com.dev.musicplayer.core.services

import android.app.Application
import android.net.Uri
import android.provider.MediaStore

data class MetaData(
    val fileName: String,
)


interface MetaDataReader {
    fun getMetaDataFromUri(contentUri: Uri): MetaData?
}

class MetaDataReaderImpl(
    private val app: Application
) : MetaDataReader {

    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        print("content uri meta data reader: $contentUri")

        if (contentUri.scheme != "content") {
            return null
        }

        val contentResolver = app.contentResolver
//        contentResolver.takePersistableUriPermission(contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val fileName = contentResolver
            .query(
                contentUri,
                arrayOf(
                    MediaStore.Audio.Media.DISPLAY_NAME
                ),
                null,
                null,
                null,
            )
            ?.use { cursor ->
                val index = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(index)

            }

        return fileName?.let { fullFileName ->
            MetaData(
                fileName = Uri.parse(fullFileName).lastPathSegment ?: return null
            )
        }
    }
}