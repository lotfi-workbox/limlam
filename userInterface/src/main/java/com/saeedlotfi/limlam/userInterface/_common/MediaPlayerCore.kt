package com.saeedlotfi.limlam.userInterface._common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.PowerManager
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerCore @Inject constructor() : MediaPlayer.OnCompletionListener,
    AudioManager.OnAudioFocusChangeListener {

    private var mediaPlayer: MediaPlayer? = null

    private var audioManager: AudioManager? = null

    private var shuffleRange: MutableList<Int> = mutableListOf()

    private var stateChangeCallback: HashMap<Int, StateChangeCallback> = hashMapOf()

    private lateinit var context: Context

    var repeatState: Int = PlaybackStateCompat.REPEAT_MODE_ALL
        private set

    var shuffleState: Int = PlaybackStateCompat.SHUFFLE_MODE_NONE
        private set

    var queue: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>? = null

    var playingIndex: Int = -1

    private val mNoisyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isPlaying()) playOrPause()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayer?.also {
            seekTo(0)
            mediaPlayer?.also { mp ->
                if (mp.duration < mp.currentPosition || mp.duration <= 0) {
                    throw Exception("File is corrupted or not supported")
                }
            }
            when (repeatState) {
                PlaybackStateCompat.REPEAT_MODE_ONE -> {
                    Thread.sleep(4000)
                    playOrPause(PlaybackStateCompat.ACTION_PLAY)
                }
                PlaybackStateCompat.REPEAT_MODE_GROUP -> {
                    Thread.sleep(2000)
                    playNext(repeat = true, byUser = false)
                }
                PlaybackStateCompat.REPEAT_MODE_ALL -> {
                    //repeat all
                    playNext(repeat = false, byUser = false)
                }
                PlaybackStateCompat.REPEAT_MODE_NONE -> {
                    //repeat off
                    seekTo(0)
                    stateChangeCallback.forEach { scc ->
                        scc.value.onMediaPlayerStateChange(StateChangeCallback.State.PAUSE)
                    }
                }
            }
        }
    }

    override fun onAudioFocusChange(focusState: Int) {
        when (focusState) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (isPlaying()) playOrPause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                //call
                if (isPlaying()) playOrPause()
            }
        }
    }

    fun init(context: Context) {
        this.context = context
    }

    fun addStateChangeCallback(stateChangeCallback: StateChangeCallback) {
        this.stateChangeCallback[stateChangeCallback.hashCode()] = stateChangeCallback
    }

    fun addStateChangeCallback(key: Int, stateChangeCallback: StateChangeCallback) {
        this.stateChangeCallback[key] = stateChangeCallback
    }

    fun removeStateChangeCallback(stateChangeCallback: StateChangeCallback) {
        this.stateChangeCallback.remove(stateChangeCallback.hashCode())
    }

    fun removeStateChangeCallback(key: Int) {
        this.stateChangeCallback.remove(key)
    }

    fun initMediaPlayer(
        iQueue: MutableList<com.saeedlotfi.limlam.domain.model.MusicDoModel>?,
        index: Int?,
        playAfterInit: Boolean,
        cp: Int = 0
    ) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            mediaPlayer?.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            mediaPlayer?.isLooping = false
        }
        iQueue?.also {
            queue = it
        }
        try {
            index?.also {
                playingIndex = index
                stateChangeCallback.forEach { scc ->
                    scc.value.onMediaPlayerStateChange(StateChangeCallback.State.INIT)
                }
                mediaPlayer?.reset()
                mediaPlayer?.setOnCompletionListener(null)
                mediaPlayer?.setOnPreparedListener {
                    mediaPlayer?.setOnCompletionListener(this)
                    if (cp > 0) {
                        seekTo(cp)
                    }
                    @Suppress("DEPRECATION")
                    if (playAfterInit) {
                        if (mediaPlayer?.isPlaying == false) playOrPause()
                    }
                }
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    File(queue?.get(playingIndex)?.path ?: "")
                ).also {
                    mediaPlayer?.setDataSource(context, it)
                }
                mediaPlayer?.prepareAsync()
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun playOrPause(forceAction: Long? = null) {

        fun play() {
            if (requestAudioFocus()) {
                context.registerReceiver(
                    mNoisyReceiver,
                    IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
                )
                mediaPlayer?.start()
                updateState()
            }
        }

        fun pause() {
            mediaPlayer?.pause()
            @Suppress("DEPRECATION")
            audioManager?.abandonAudioFocus(this)
            updateState()
        }

        when {
            mediaPlayer != null && forceAction == PlaybackStateCompat.ACTION_PLAY -> play()
            mediaPlayer != null && forceAction == PlaybackStateCompat.ACTION_PAUSE -> pause()
            mediaPlayer?.isPlaying == false -> play()
            mediaPlayer?.isPlaying == true -> pause()
        }

    }

    fun playPrevious() {
        if (!queue.isNullOrEmpty()) {
            if (mediaPlayer != null && mediaPlayer!!.currentPosition >= 5000L) {
                seekTo(0)
            } else {
                val playing = mediaPlayer?.isPlaying ?: false
                when {
                    shuffleState == PlaybackStateCompat.SHUFFLE_MODE_ALL -> {
                        when {
                            shuffleRange.isNotEmpty() -> {
                                playingIndex =
                                    shuffleRange.random()
                                shuffleRange.remove(playingIndex)
                                initMediaPlayer(
                                    queue!!,
                                    playingIndex, playing
                                )
                            }
                            else -> {
                                queue?.also {
                                    shuffleRange =
                                        (0 until it.size).toMutableList()
                                    playNext(repeat = true, byUser = false)
                                }
                            }
                        }
                    }
                    playingIndex > 0 -> {
                        initMediaPlayer(
                            queue!!,
                            --playingIndex,
                            playing
                        )
                    }
                    else -> {
                        playingIndex = queue!!.size - 1
                        initMediaPlayer(
                            queue!!,
                            playingIndex, playing
                        )
                    }
                }
                updateState()
            }
        }
    }

    fun playNext(repeat: Boolean, byUser: Boolean) {
        if (!queue.isNullOrEmpty()) {
            when {
                byUser -> {
                    val playing = mediaPlayer?.isPlaying ?: false
                    when {
                        shuffleState == PlaybackStateCompat.SHUFFLE_MODE_ALL -> {
                            when {
                                shuffleRange.isNotEmpty() -> {
                                    playingIndex =
                                        shuffleRange.random()
                                    shuffleRange.remove(playingIndex)
                                    initMediaPlayer(
                                        queue!!,
                                        playingIndex, playing
                                    )
                                }
                                else -> {
                                    queue?.also {
                                        shuffleRange =
                                            (0 until it.size).toMutableList()
                                        if (repeat) {
                                            playNext(repeat = true, byUser = false)
                                        }
                                    }
                                }
                            }
                        }
                        playingIndex + 1 < queue!!.size -> {
                            initMediaPlayer(
                                queue!!,
                                ++playingIndex,
                                playing
                            )
                        }
                        repeat -> {
                            playingIndex = 0
                            initMediaPlayer(
                                queue!!,
                                playingIndex, playing
                            )
                        }
                    }
                    updateState()
                }
                else -> {
                    when {
                        shuffleState == PlaybackStateCompat.SHUFFLE_MODE_ALL -> {
                            when {
                                shuffleRange.isNotEmpty() -> {
                                    playingIndex =
                                        shuffleRange.random()
                                    shuffleRange.remove(playingIndex)
                                    initMediaPlayer(
                                        queue!!,
                                        playingIndex, true
                                    )
                                    updateState()
                                }
                                else -> {
                                    queue?.also {
                                        shuffleRange =
                                            (0 until it.size).toMutableList()
                                        if (repeat) {
                                            playNext(repeat = true, byUser = false)
                                        }
                                    }
                                }
                            }
                        }
                        playingIndex + 1 < queue!!.size -> {
                            initMediaPlayer(
                                queue!!,
                                ++playingIndex,
                                true
                            )
                            updateState()
                        }
                        repeat -> {
                            playingIndex = 0
                            initMediaPlayer(
                                queue!!,
                                playingIndex, true
                            )
                            updateState()
                        }
                        playingIndex + 1 == queue!!.size -> {
                            updateState()
                        }
                    }
                }
            }
        }
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    fun getCurrentPosition(): Long = mediaPlayer?.currentPosition?.toLong() ?: 0

    fun getDuration() = mediaPlayer?.duration

    fun getCurrentPlaying(): com.saeedlotfi.limlam.domain.model.MusicDoModel? = queue?.get(playingIndex)

    fun seekTo(pos: Int) {
        mediaPlayer?.seekTo(pos)
        stateChangeCallback.forEach { scc ->
            scc.value.onMediaPlayerStateChange(StateChangeCallback.State.SEEK)
        }
    }

    fun changeRepeatState(repeatMode: Int, onComplete: () -> Unit) {
        repeatState = repeatMode
        if (shuffleState == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
            queue?.also {
                shuffleRange = (0 until it.size).toMutableList()
                if (isPlaying()) {
                    shuffleRange.remove(playingIndex)
                }
            }
        }
        onComplete()
    }

    fun changeShuffleState(shuffleMode: Int, onComplete: () -> Unit) {
        shuffleState = shuffleMode
        if (shuffleState == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
            queue?.also {
                shuffleRange = (0 until it.size).toMutableList()
                if (isPlaying()) {
                    shuffleRange.remove(playingIndex)
                }
            }
        }
        onComplete()
    }

    private fun updateState() {
        if (isPlaying()) {
            stateChangeCallback.forEach { scc ->
                scc.value.onMediaPlayerStateChange(StateChangeCallback.State.PLAY)
            }
        } else {
            stateChangeCallback.forEach { scc ->
                scc.value.onMediaPlayerStateChange(StateChangeCallback.State.PAUSE)
            }
        }
    }

    private fun requestAudioFocus(): Boolean {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager?.requestAudioFocus(
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this).build()
            )
        } else {
            @Suppress("DEPRECATION")
            audioManager?.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    interface StateChangeCallback {

        fun onMediaPlayerStateChange(state: State)

        enum class State {
            INIT, PLAY, PAUSE, SEEK
        }

    }

}