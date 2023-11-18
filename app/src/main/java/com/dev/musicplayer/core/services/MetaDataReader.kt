package com.dev.musicplayer.core.services

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

data class MetaData(
    val fileName: String,
    val uri: Uri,
)


interface MetaDataReader {
    fun getMetaDataFromUri(contentUri: Uri, contentResolver: ContentResolver): MetaData?

//    fun getRealPathFromUri( uri: Uri): String?
}

class MetaDataReaderImpl(

) : MetaDataReader {





    override fun getMetaDataFromUri(contentUri: Uri, contentResolver: ContentResolver): MetaData? {
        print("content uri meta data reader: $contentUri")
        val videoList = mutableListOf<MetaData>()

        if (contentUri.scheme != "content") {
            return null
        }

        val fileName = contentResolver
            .query(
                contentUri,
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.SIZE,
                ),
                null,
                null,
                null,
            )
            ?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

//                val index = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//                val filePath = cursor.getString(columnIndex)

                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val data = cursor.getString(dataColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)

                    val uri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    Log.d("TAG", "getMetaDataFromUri: $data")
                    Log.d("TAG", "getMetaDataFromUri: $uri + $size + $duration")


                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    videoList += MetaData(name, uri = contentUri)
                }



//                Log.d("TAG", "getMetaDataFromUri: $filePath")

//                cursor.moveToFirst()
//                cursor.getString(index)

            }

        Log.d("TAG", "getMetaDataFromUri: ${videoList.size}")

        return videoList.first()
//
//        return fileName?.let { fullFileName ->
//            MetaData(
//                fileName = Uri.parse(fullFileName).lastPathSegment ?: return null
//            )
//        }
    }
}