package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.domain.repositories.MusicRepository
import javax.inject.Inject

class GetMusicsUseCase @Inject constructor(private val musicRepository: MusicRepository) {
     operator fun invoke() = musicRepository.getMusicsStorage()
}