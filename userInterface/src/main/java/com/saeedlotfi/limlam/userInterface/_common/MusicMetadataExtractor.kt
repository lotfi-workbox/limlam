package com.saeedlotfi.limlam.userInterface._common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.*
import android.util.Log
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class MusicMetadataExtractor @Inject constructor() {

    private var mmr: MediaMetadataRetriever? = null

    fun initRetriever() {
        mmr = MediaMetadataRetriever()
    }

    private fun setDataSource(musicPath: String): Boolean {
        var isFileCorrupted = true
        File(musicPath).also { music ->
            if (music.exists() && music.isAbsolute && music.length() != 0L && music.isFile) {
                try {
                    mmr?.setDataSource(musicPath)
                    isFileCorrupted = false
                } catch (e: Exception) {
                    Log.i("MusicMetadataExtractor", "brokenFile => path:$musicPath")
                }
            }
        }
        return !isFileCorrupted
    }

    fun extractPicture(musicPath: String, onComplete: (image: Bitmap?) -> Unit) {
        try {
            if (setDataSource(musicPath)) {
                mmr?.embeddedPicture?.also { byteArray ->
                    BitmapFactory.decodeByteArray(
                        byteArray,
                        0,
                        byteArray.size,
                        BitmapFactory.Options().also { opt ->
                            opt.inSampleSize = 2
                            opt.inPreferredConfig = Bitmap.Config.RGB_565
                        }).also { bitmap ->
                        onComplete(bitmap)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            onComplete(null)
            println(e.stackTrace)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun extractPictureAsync(
        musicPath: String,
        onComplete: (image: Bitmap?) -> Unit
    ) {
        GlobalScope.launch {
            extractPicture(musicPath) {
                MainScope().launch { onComplete(it) }
            }
        }
    }

    fun extractData(
        musicPath: String,
        ifSuccess: (title: String, artist: String, album: String, genre: String, duration: String) -> Unit
    ) {
        try {
            if (setDataSource(musicPath)) {
                ifSuccess(
                    mmr?.extractMetadata(METADATA_KEY_TITLE) ?: Constants.UNKNOWN,
                    mmr?.extractMetadata(METADATA_KEY_ARTIST) ?: Constants.UNKNOWN,
                    mmr?.extractMetadata(METADATA_KEY_ALBUM) ?: Constants.UNKNOWN,
                    mmr?.extractMetadata(METADATA_KEY_GENRE) ?: Constants.UNKNOWN,
                    mmr?.extractMetadata(METADATA_KEY_DURATION) ?: "0"
                )
            }
        } catch (e: Exception) {
            println(e.stackTrace)
        }
    }

    fun closeRetriever() {
        mmr?.release()
        mmr = null
    }

}