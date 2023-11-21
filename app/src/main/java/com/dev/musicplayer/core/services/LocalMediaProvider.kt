package com.dev.musicplayer.core.services

import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.database.getStringOrNull
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import java.io.File

class LocalMediaProvider(
	private val applicationContext: Context
){
	fun getSongItemFromContentUri(uri: Uri): MediaAudioItem?{

		var displayName:String? = null

		if(uri.scheme == "content"){
			Log.d(TAG, "Uri scheme is content")
			applicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
				cursor.moveToFirst()
				displayName = cursor.getStringOrNull(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
			}
		}else{
			Log.d(TAG, "Uri scheme is file")
			displayName = uri.path?.split("/")?.lastOrNull().toString()
		}

		return if(displayName != null){
			Log.d(TAG, "display name is not null")
			getMediaSong().first{ displayName == it.name }
		}else{
			Log.d(TAG, "display name is null")
			null
		}
	}

	private var selectionClause: String? = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"
	private var selectionArg = arrayOf("1")
	private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

	fun getMediaVideosFlow(

	): Flow<List<MediaAudioItem>> = callbackFlow {
		val observer = object : ContentObserver(null) {
			override fun onChange(selfChange: Boolean) {
				trySend(getMediaSong())
			}
		}
		applicationContext.contentResolver.registerContentObserver(MUSIC_COLLECTION_URI, true, observer)
		// initial value
		trySend(getMediaSong())
		// close
		awaitClose { applicationContext.contentResolver.unregisterContentObserver(observer) }
	}.flowOn(Dispatchers.IO).distinctUntilChanged()

	private fun getMediaSong(
	): List<MediaAudioItem> {
		val mediaAudioItems = mutableListOf<MediaAudioItem>()
		applicationContext.contentResolver.query(
			MUSIC_COLLECTION_URI,
			AUDIO_PROJECTION,
			selectionClause,
			selectionArg,
			sortOrder
		)?.use { cursor ->

			val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
			val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
			val artistColumn  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
			val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
			val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
			val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
			val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)

			while (cursor.moveToNext()) {
				val id = cursor.getLong(idColumn)
				val absolutePath = cursor.getString(dataColumn)
				val title = cursor.getString(titleColumn)
				val artist = cursor.getString(artistColumn)
				val uri = ContentUris.withAppendedId(MUSIC_COLLECTION_URI, id)

				val coverBytes = MediaMetadataRetriever().apply {
					setDataSource(applicationContext, uri)
				}.embeddedPicture
				val songCover: Bitmap? = if (coverBytes != null)
					BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.size) else null

				mediaAudioItems.add(
					MediaAudioItem(
						id = id,
						name = title,
						artist =artist,
						absolutePath = absolutePath,
						duration = cursor.getLong(durationColumn),
						uri = uri,
						size = cursor.getLong(sizeColumn),
						dateModified = cursor.getLong(dateModifiedColumn),
						artWork = songCover,
					)
				)
			}
		}
		return mediaAudioItems.filter { File(it.absolutePath).exists() }
	}

	companion object {

		const val TAG = "Local Media Provider"

		val MUSIC_COLLECTION_URI: Uri
			get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
			} else {
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
			}

		val AUDIO_PROJECTION = arrayOf(
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.AudioColumns.DISPLAY_NAME,
			MediaStore.Audio.AudioColumns.ARTIST,
			MediaStore.Audio.AudioColumns.TITLE,
			MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.SIZE,
			MediaStore.Audio.Media.DATE_MODIFIED
		)
	}

}