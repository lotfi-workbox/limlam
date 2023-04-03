package com.saeedlotfi.limlam.userInterface.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.saeedlotfi.limlam.userInterface._common.Constants
import com.saeedlotfi.limlam.userInterface._common.BitmapMemoryCache
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore.StateChangeCallback.State
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.ViewModelFactory
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts.PreviewerUiNormal
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.viewModels.PreviewerViewModel
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PreviewerActivity : com.saeedlotfi.limlam.userInterface._common.BaseActivity() {

    @Inject
    lateinit var userInterface: UiComponentFactory<PreviewerUiNormal>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    private lateinit var viewModel: PreviewerViewModel

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                doubleTabToExit {
                    if (mediaPlayerCore.isPlaying()) mediaPlayerCore.playOrPause()
                }
            }
        }

    private val stateChangeCallback: MediaPlayerCore.StateChangeCallback =
        object : MediaPlayerCore.StateChangeCallback {
            override fun onMediaPlayerStateChange(state: State) {
                when (state) {
                    State.PLAY, State.PAUSE -> {
                        userInterface.view.cmptController.updatePlayButton(mediaPlayerCore.isPlaying())
                        onMediaPlayerStateChange(State.SEEK)
                    }
                    State.INIT -> {
                        userInterface.view.updateImagesAndLabels(this@PreviewerActivity)
                    }
                    State.SEEK -> {
                        if (mediaPlayerCore.isPlaying())
                            userInterface.view.cmptSeekbar.startUpdateSeekbar(
                                mediaPlayerCore.getDuration() ?: 0,
                                mediaPlayerCore.getCurrentPosition().toInt()
                            )
                        else userInterface.view.cmptSeekbar.stopUpdateSeekbar()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserInterfaceComponent().inject(this)
        mediaPlayerCore.init(this)
        mediaPlayerCore.addStateChangeCallback(stateChangeCallback)
        userInterface.createComponent(UiContext.create(this), PreviewerUiNormal::class)
        setContentView(userInterface.view.rootView)
        ViewModelProvider(this).also {
            viewModel = it[PreviewerViewModel::class.java]
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        userInterface.view.configUi()
    }

    override fun onStart() {
        mediaPlayerCore.changeRepeatState(PlaybackStateCompat.REPEAT_MODE_NONE) {}
        mediaPlayerCore.changeShuffleState(PlaybackStateCompat.SHUFFLE_MODE_NONE) {}
        getStoragePermission {
            getMusicFileAndPlay(interceptIntentAndReturnFile(intent))
        }
        super.onStart()
    }

    private fun getMusicFileAndPlay(file: File?) {
        file?.also first@{
            if (file.exists()) {
                viewModel.getFolderFromDb(file.parent!!.hashCode().toLong()) { fm ->
                    if (fm == null) {
                        createMusicModelFromFile(file)?.also {
                            mediaPlayerCore.initMediaPlayer(mutableListOf(it), 0, true)
                        }
                    }else{
                        if (!fm.musics.isNullOrEmpty()) {
                            fm.musics?.forEach { musicModel ->
                                if (file.path == musicModel.path) {
                                    mediaPlayerCore.initMediaPlayer(
                                        fm.musics?.toMutableList(),
                                        0,
                                        true
                                    )
                                    return@forEach
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createMusicModelFromFile(file: File?): com.saeedlotfi.limlam.domain.model.MusicDoModel? {
        if (file != null && file.exists()) {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(file.path)
            if (mmr.embeddedPicture?.also { byteArray ->
                    var out: FileOutputStream
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size,
                        BitmapFactory.Options().also { opt ->
                            opt.inSampleSize = 2
                            opt.inPreferredConfig = Bitmap.Config.RGB_565
                        }).also { bitmap ->
                        File(
                            Constants.cachePath, "${this@PreviewerActivity::class.hashCode()}.jpeg"
                        ).also { mFile ->
                            if (mFile.exists()) {
                                mFile.delete()
                            }
                            out = FileOutputStream(mFile)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out)
                            out.flush()
                            out.close()
                        }
                    }
                } == null) {
                File(
                    Constants.cachePath, "${this@PreviewerActivity::class.hashCode()}.jpeg"
                ).also { mFile ->
                    if (mFile.exists()) {
                        mFile.delete()
                    }
                }
            }
            return com.saeedlotfi.limlam.domain.model.MusicDoModel(
                name = file.name,
                type = null,
                path = file.path,
                parent = file.parent,
                parentName = file.parentFile?.name,
                artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    ?: "Unknown",
                album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    ?: "Unknown",
                genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: "Unknown",
                duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                    ?: "Unknown",
                id = file.path.hashCode().toLong()
            )
        } else return null
    }

    private fun interceptIntentAndReturnFile(intent: Intent): File? {
        val data: Uri? = intent.data
        if (data != null && intent.action != null && intent.action == Intent.ACTION_VIEW) {
            val scheme: String? = data.scheme
            var path: String? = null
            if (scheme == "file") {
                path = data.path
            } else if (scheme == "content") {
                contentResolver.openInputStream(data)?.also { iStream ->
                    File(externalCacheDir, "LimLam.mp3").also { file ->
                        if (file.exists()) {
                            file.delete()
                        }
                        file.createNewFile()
                        val outputStream = FileOutputStream(file)
                        iStream.copyTo(outputStream)
                        outputStream.flush()
                        path = file.path
                    }
                    iStream.close()

                }
            }
            setIntent(Intent())
            path?.also { return File(it) }
        }
        return null
    }

    override fun onDestroy() {
        userInterface.view.cmptSeekbar.stopUpdateSeekbar()
        mediaPlayerCore.removeStateChangeCallback(stateChangeCallback)
        BitmapMemoryCache.evictAll()
        super.onDestroy()
    }

}
