package com.saeedlotfi.limlam.userInterface._common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.services.MusicPlayerService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationCenter @Inject constructor(private val mediaPlayerCore: MediaPlayerCore) {

    private lateinit var mediaSessionState: MediaSessionState

    private var notification: NotificationCompat.Builder? = null

    fun init(mediaSessionState: MediaSessionState) {
        this.mediaSessionState = mediaSessionState
    }

    fun updatePlayerNotification(context: Context) {

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.notify(
            MusicPlayerService::class.hashCode(), getPlayerNotification(context)
        )

    }

    fun getPlayerNotification(context: Context): Notification {

        var builder: Notification? = null

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (notification == null) {
            val notificationChanel = createNotificationChannel(
                notificationManager,
                "Music Player Controller"
            )
            NotificationCompat.Builder(context, notificationChanel).also {
                it.setContentIntent(mediaSessionState.getMediaSessionActivity())
                it.setSmallIcon(R.drawable.ic_icon)
                mediaPlayerCore.getCurrentPlaying().also { mm ->
                    if (mm?.title != null && mm.title != "Unknown") {
                        it.setContentTitle(mm.title)
                    } else {
                        it.setContentTitle(mm?.name ?: "Unknown")
                    }
                }

                it.setContentText(
                    "${mediaPlayerCore.getCurrentPlaying()?.artist ?: "Unknown"} " +
                            "|| ${mediaPlayerCore.getCurrentPlaying()?.album ?: "Unknown"}"
                )
                it.setOnlyAlertOnce(true)
                it.setShowWhen(false)

                it.priority = NotificationCompat.PRIORITY_LOW
                it.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                ).also { pi ->
                    it.addAction(R.drawable.ic_rewind, "rewind", pi)
                }

                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                ).also { pi ->
                    if (mediaSessionState.getMediaSessionPlayingState()) {
                        it.addAction(R.drawable.ic_pause, "pause", pi)
                    } else {
                        it.addAction(R.drawable.ic_play, "play", pi)
                    }
                }

                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                ).also { pi ->
                    it.addAction(R.drawable.ic_forward, "forward", pi)
                }

                it.setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionState.getMediaSessionToken())
                )

                notification = it
                builder = it.build()
            }
        } else {
            notification?.also { notification ->

                mediaPlayerCore.getCurrentPlaying().also { mm ->
                    if (mm?.title != null && mm.title != "Unknown") {
                        notification.setContentTitle(mm.title)
                    } else {
                        notification.setContentTitle(mm?.name ?: "Unknown")
                    }
                }
                notification.setContentText(
                    "${mediaPlayerCore.getCurrentPlaying()?.artist ?: "Unknown"} ||" +
                            " ${mediaPlayerCore.getCurrentPlaying()?.album ?: "Unknown"}"
                )

                notification.setContentIntent(mediaSessionState.getMediaSessionActivity())
                builder = notification.build()

                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                ).also { pi ->
                    if (mediaSessionState.getMediaSessionPlayingState()) {
                        @Suppress("DEPRECATION")
                        builder?.actions!![1] =
                            Notification.Action(R.drawable.ic_pause, "pause", pi)
                    } else {
                        @Suppress("DEPRECATION")
                        builder?.actions!![1] =
                            Notification.Action(R.drawable.ic_play, "play", pi)
                    }
                }
            }
        }

        return builder!!
    }

    interface MediaSessionState {

        fun getMediaSessionActivity(): PendingIntent

        fun getMediaSessionToken(): MediaSessionCompat.Token

        fun getMediaSessionPlayingState(): Boolean

    }

    companion object {

        fun getProgressNotification(
            context: Context, channelName: String, title: String
        ): Notification {

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            return NotificationCompat.Builder(
                context,
                createNotificationChannel(
                    notificationManager,
                    channelName
                )
            ).setSmallIcon(R.drawable.ic_sync)
                .setContentTitle(title)
                .setProgress(100, 0, true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true)
                .build()
        }

        fun createNotificationChannel(
            notificationManager: NotificationManager, channelName: String
        ): String {
            val channelId = channelName.hashCode().toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_NONE
                ).also { nChannel ->
                    nChannel.lightColor = Color.MAGENTA
                    nChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    notificationManager.createNotificationChannel(nChannel)
                }
            }
            return channelId
        }

    }

}