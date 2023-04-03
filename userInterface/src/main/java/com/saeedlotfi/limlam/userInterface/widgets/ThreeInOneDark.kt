package com.saeedlotfi.limlam.userInterface.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.RemoteViews
import androidx.media.session.MediaButtonReceiver
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface._common.Constants
import com.saeedlotfi.limlam.userInterface._common.BitmapDiskCache
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore.StateChangeCallback.State
import com.saeedlotfi.limlam.userInterface.activities.main.MainActivity
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import javax.inject.Inject

class ThreeInOneDark : AppWidgetProvider() {

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    private var userInterface: RemoteViews =
        RemoteViews(Constants.packageName, R.layout.three_in_one_dark)

    private var appWidgetManager: AppWidgetManager? = null

    private var appWidgetIds: IntArray? = null

    private val stateChangeCallback: MediaPlayerCore.StateChangeCallback =
        object : MediaPlayerCore.StateChangeCallback {
            override fun onMediaPlayerStateChange(state: State) {
                when (state) {
                    State.INIT -> {
                        updateImagesAndLabels(userInterface)
                    }
                    State.PLAY, State.PAUSE -> {
                        if (mediaPlayerCore.isPlaying()) {
                            userInterface.setImageViewResource(
                                R.id.btPlay,
                                R.drawable.widget_pause_dark
                            )
                        } else {
                            userInterface.setImageViewResource(
                                R.id.btPlay,
                                R.drawable.widget_play_dark
                            )
                        }
                    }
                    State.SEEK -> {}
                }
                if (appWidgetIds?.isNotEmpty() == true)
                    appWidgetManager?.updateAppWidget(appWidgetIds, userInterface)
            }
        }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (appWidgetManager == null) appWidgetManager = AppWidgetManager.getInstance(context)
        if (context != null) buttonActions(context, userInterface)
        appWidgetIds = intent?.extras?.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)
        when (intent?.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                getUserInterfaceComponent(context).inject(this)
                mediaPlayerCore.addStateChangeCallback(this::class.hashCode(), stateChangeCallback)
            }
            AppWidgetManager.ACTION_APPWIDGET_DELETED -> {
                getUserInterfaceComponent(context).inject(this)
                mediaPlayerCore.removeStateChangeCallback(this::class.hashCode())
            }
        }
    }

    private fun updateImagesAndLabels(views: RemoteViews) {
        mediaPlayerCore.getCurrentPlaying().also { playingModel ->
            if (playingModel?.title != null && playingModel.title != "Unknown") {
                views.setTextViewText(R.id.tvName, playingModel.title)
            } else {
                views.setTextViewText(R.id.tvName, playingModel?.name ?: "Music Name")
            }
            views.setTextViewText(R.id.tvArtist, playingModel?.artist ?: "Artist")
            views.setTextViewText(R.id.tvAlbum, playingModel?.album ?: "Album")
            playingModel?.path.also {
                if (it == null) {
                    views.setImageViewResource(R.id.ivAlbum, R.drawable.ic_note)
                } else {
                    BitmapDiskCache.getImage(
                        mediaPlayerCore.getCurrentPlaying()?.id?.toInt() ?: 0
                    ) { bmp ->
                        if (bmp != null) {
                            views.setImageViewBitmap(R.id.ivAlbum, bmp)
                        } else {
                            views.setImageViewResource(R.id.ivAlbum, R.drawable.ic_note)
                        }
                    }
                }
            }
        }
    }

    private fun buttonActions(context: Context?, views: RemoteViews) {

        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        ).also { pi ->
            views.setOnClickPendingIntent(R.id.btPrevious, pi)
        }

        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_PLAY_PAUSE
        ).also { pi ->
            views.setOnClickPendingIntent(R.id.btPlay, pi)
        }

        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
        ).also { pi ->
            views.setOnClickPendingIntent(R.id.btNext, pi)
        }

        Intent(context, MainActivity::class.java).also { intent ->
            intent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            val pIntent = PendingIntent.getActivity(
                context,
                this@ThreeInOneDark::class.hashCode(),
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.fl_widget, pIntent)
        }

    }

}
