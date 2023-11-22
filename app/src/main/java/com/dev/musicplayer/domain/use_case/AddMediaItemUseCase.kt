package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.domain.service.PlaybackController
import javax.inject.Inject

//class AddMediaItemsFromLocalDBUseCase @Inject constructor(
//    private val playbackController: PlaybackController
//) {
//    operator fun invoke(musics: List<Song>? = null , ) {
//        if (musics != null)
//            playbackController.addMediaItemsFromLocalDB(musics)
//    }
//}

class AddMediaItemFromStorageUseCase @Inject constructor(
    private val playbackController: PlaybackController
) {
    operator fun invoke(musics: List<MediaAudioItem>? = null , ) {
        if (musics != null)
            playbackController.addMediaItemsFromStorage(musics)
    }
}