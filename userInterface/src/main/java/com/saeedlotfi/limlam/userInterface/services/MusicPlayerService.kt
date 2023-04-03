package com.saeedlotfi.limlam.userInterface.services

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.*
import android.content.res.Configuration
import android.media.*
import android.os.*
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.saeedlotfi.limlam.domain.useCase.AddToRecentlyPlayedUseCase
import com.saeedlotfi.limlam.domain.useCase.GetStateUseCase
import com.saeedlotfi.limlam.domain.useCase.SaveStateUseCase
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface._common.Constants
import com.saeedlotfi.limlam.userInterface._common.isServiceRunning
import com.saeedlotfi.limlam.userInterface._common.sendBroadCast
import com.saeedlotfi.limlam.userInterface._common.BitmapDiskCache
import com.saeedlotfi.limlam.userInterface._common.BroadcastCenter
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore.StateChangeCallback.*
import com.saeedlotfi.limlam.userInterface._common.NotificationCenter
import com.saeedlotfi.limlam.userInterface.activities.main.MainActivity
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.widgets.ThreeInOneDark
import com.saeedlotfi.limlam.userInterface.widgets.ThreeInOneLight
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class MusicPlayerService : MediaBrowserServiceCompat() {

    companion object {
        fun startService(context: Context) {
            if (!context.isServiceRunning(MusicPlayerService::class.java)) {
                Intent(context, MusicPlayerService::class.java).also { intent ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent)
                    } else {
                        context.startService(intent)
                    }
                }
            }
        }
    }

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    @Inject
    lateinit var notificationCenter: NotificationCenter

    @Inject
    lateinit var getStateUseCase: GetStateUseCase

    @Inject
    lateinit var saveStateUseCase: SaveStateUseCase

    @Inject
    lateinit var addToRecentlyPlayedUseCase: AddToRecentlyPlayedUseCase

    private var mediaSessionCompat: MediaSessionCompat? = null

    private var playbackStateBuilder: PlaybackStateCompat.Builder? = null

    private var mMediaBrowserCompat: MediaBrowserCompat? = null

    private var serviceJob = SupervisorJob()

    private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val mMediaSessionCallback: MediaSessionCompat.Callback =
        object : MediaSessionCompat.Callback() {

            override fun onPlay() {
                super.onPlay()
                if (!mediaPlayerCore.isPlaying()) mediaPlayerCore.playOrPause()
            }

            override fun onPause() {
                super.onPause()
                if (mediaPlayerCore.isPlaying()) mediaPlayerCore.playOrPause()
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                mediaPlayerCore.playNext(repeat = true, byUser = true)
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                mediaPlayerCore.playPrevious()
            }

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                mediaPlayerCore.seekTo(pos.toInt())
                if (mediaPlayerCore.isPlaying()) {
                    setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                } else if (!mediaPlayerCore.isPlaying()) {
                    setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
                }
            }

            override fun onSetShuffleMode(shuffleMode: Int) {
                super.onSetShuffleMode(shuffleMode)
                mediaPlayerCore.changeShuffleState(shuffleMode) {}
            }

            override fun onSetRepeatMode(repeatMode: Int) {
                super.onSetRepeatMode(repeatMode)
                mediaPlayerCore.changeRepeatState(repeatMode) {}
            }

        }

    private val connectionCallback: MediaBrowserCompat.ConnectionCallback =
        object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {
                super.onConnected()
                BroadcastCenter.updateWidget(
                    this@MusicPlayerService, ThreeInOneLight::class.java
                )
                BroadcastCenter.updateWidget(
                    this@MusicPlayerService, ThreeInOneDark::class.java
                )
            }
        }

    private val notificationStateCallback: NotificationCenter.MediaSessionState =
        object : NotificationCenter.MediaSessionState {
            override fun getMediaSessionActivity(): PendingIntent {
                return mediaSessionCompat!!.controller.sessionActivity
            }

            override fun getMediaSessionToken(): MediaSessionCompat.Token {
                return mediaSessionCompat!!.sessionToken
            }

            override fun getMediaSessionPlayingState(): Boolean {
                return mediaPlayerCore.isPlaying()
            }

        }

    private val stateChangeCallback: MediaPlayerCore.StateChangeCallback =
        object : MediaPlayerCore.StateChangeCallback {
            override fun onMediaPlayerStateChange(state: State) {
                when (state) {
                    State.PLAY -> {
                        startForeground(
                            this@MusicPlayerService::class.hashCode(),
                            notificationCenter.getPlayerNotification(this@MusicPlayerService)
                        )
                        mediaSessionCompat!!.isActive = true
                        setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                    }
                    State.PAUSE -> {
                        setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
                        saveStates {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                stopForeground(STOP_FOREGROUND_DETACH)
                            } else {
                                @Suppress("DEPRECATION")
                                stopForeground(false)
                            }
                        }
                    }
                    State.INIT -> {
                        addPlayingToRecentlyPlayed()
                    }
                    else -> {}
                }
                @Suppress("DEPRECATION")
                BitmapDiskCache.getImage(
                    mediaPlayerCore.getCurrentPlaying()?.id?.toInt() ?: 0
                ) { bmp ->
                    val mmd = MediaMetadata.Builder()
                        .putString(
                            MediaMetadata.METADATA_KEY_TITLE,
                            mediaPlayerCore.getCurrentPlaying()?.title
                        )
                        .putString(
                            MediaMetadata.METADATA_KEY_ARTIST,
                            mediaPlayerCore.getCurrentPlaying()?.artist
                        )
                        .putString(
                            MediaMetadata.METADATA_KEY_ALBUM,
                            mediaPlayerCore.getCurrentPlaying()?.album
                        )
                        .putString(
                            MediaMetadata.METADATA_KEY_GENRE,
                            mediaPlayerCore.getCurrentPlaying()?.genre
                        )
                        .putLong(
                            MediaMetadata.METADATA_KEY_DURATION,
                            mediaPlayerCore.getCurrentPosition()
                        )
                    if (bmp != null) {
                        mmd.putBitmap(MediaMetadata.METADATA_KEY_ART, bmp)
                    } else {
                        mmd.putBitmap(
                            MediaMetadata.METADATA_KEY_ART,
                            AppCompatResources.getDrawable(
                                this@MusicPlayerService,
                                R.drawable.ic_note
                            )?.toBitmap()
                        )
                    }
                    mediaSessionCompat?.setMetadata(MediaMetadataCompat.fromMediaMetadata(mmd.build()))
                }
                notificationCenter.updatePlayerNotification(this@MusicPlayerService)
            }
        }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent)
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        BroadcastCenter.updateWidget(this, ThreeInOneLight::class.java)
        BroadcastCenter.updateWidget(this, ThreeInOneDark::class.java)
        super.onConfigurationChanged(newConfig)
    }

    override fun onGetRoot(
        clientPackageName: String, clientUid: Int, rootHints: Bundle?
    ): BrowserRoot? {
        return if (TextUtils.equals(clientPackageName, packageName))
            BrowserRoot(getString(R.string.app_name), null) else null
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null)
    }

    override fun onCreate() {
        super.onCreate()
        getUserInterfaceComponent().inject(this)
        mediaPlayerCore.init(this)
        mediaPlayerCore.addStateChangeCallback(stateChangeCallback)
        notificationCenter.init(notificationStateCallback)
        initMediaSession()
        mMediaBrowserCompat = MediaBrowserCompat(
            this,
            ComponentName(this, this@MusicPlayerService::class.java),
            connectionCallback,
            null
        )
        mMediaBrowserCompat!!.connect()
        startForeground(
            this@MusicPlayerService::class.hashCode(),
            notificationCenter.getPlayerNotification(this)
        )
        loadStates()
    }

    override fun onDestroy() {
        serviceJob.cancel()
        mediaPlayerCore.removeStateChangeCallback(stateChangeCallback)
        Process.killProcess(Process.myPid())
        super.onDestroy()
    }

    private fun initMediaSession() {
        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.setClass(this, MediaButtonReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            mediaButtonIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) FLAG_IMMUTABLE else 0
        )

        val mediaButtonReceiver = ComponentName(this, MediaButtonReceiver::class.java)
        mediaSessionCompat =
            MediaSessionCompat(this, "MusicPlayerService", mediaButtonReceiver, pendingIntent)
        mediaSessionCompat?.isActive = true
        mediaSessionCompat?.setCallback(mMediaSessionCallback)

        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            val pIntent = PendingIntent.getActivity(
                this,
                this@MusicPlayerService::class.hashCode(),
                it,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE else FLAG_UPDATE_CURRENT
            )
            mediaSessionCompat?.setSessionActivity(pIntent)
        }

        sessionToken = mediaSessionCompat?.sessionToken
    }

    private fun setMediaPlaybackState(state: Int) {
        if (playbackStateBuilder == null) {
            playbackStateBuilder = PlaybackStateCompat.Builder()
            playbackStateBuilder?.setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SEEK_TO or
                        PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE or
                        PlaybackStateCompat.ACTION_SET_REPEAT_MODE
            )
        }
        playbackStateBuilder?.setState(state, mediaPlayerCore.getCurrentPosition(), 1f)
        mediaSessionCompat?.setPlaybackState(playbackStateBuilder?.build())
    }

    private fun addPlayingToRecentlyPlayed() = scope.launch {
        addToRecentlyPlayedUseCase(
            mediaPlayerCore.getCurrentPlaying()!!
        ) {
            sendBroadCast(
                Constants.BC_ACTION_REFRESH_PLAYLISTS,
                Constants.BC_KEY_REFRESH_RECENTLY_PLAYED
            )
        }
    }

    private fun loadStates() {
        getStateUseCase {
            if (it != null)
                mediaPlayerCore.initMediaPlayer(
                    it.queue,
                    it.playingIndex,
                    false,
                    it.seekbarPosition ?: 0
                )
        }
    }

    private fun saveStates(onComplete: (com.saeedlotfi.limlam.domain.model.StateDoModel) -> Unit) {
        getStateUseCase {
            if (it != null) scope.launch {
                saveStateUseCase(
                    it.also {
                        it.queue = mediaPlayerCore.queue
                        it.playingIndex = mediaPlayerCore.playingIndex
                        it.seekbarPosition = mediaPlayerCore.getCurrentPosition().toInt()
                        mediaPlayerCore.shuffleState
                    },
                    onComplete
                )
            }
        }
    }

}