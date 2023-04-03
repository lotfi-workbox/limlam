@file:Suppress("MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.media.session.PlaybackStateCompat
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface._common.Constants
import com.saeedlotfi.limlam.userInterface._common.sendBroadCast
import com.saeedlotfi.limlam.userInterface._common.DialogsManager
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MusicMetadataExtractor
import com.saeedlotfi.limlam.userInterface.fragments.queue.QueueDialog
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi.BottomSheetUiNormal.*
import com.saeedlotfi.limlam.userInterface.layouts.components.*
import com.saeedlotfi.limlam.userInterface.viewModels.MainViewModel
import javax.inject.Inject

open class BottomSheetUiNormal @Inject constructor(
    protected open var mediaPlayerCore: MediaPlayerCore,
    open var metadataExtractor: MusicMetadataExtractor
) : BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout
    lateinit var viewModel: MainViewModel

    lateinit var cmptFlat: FlatUiComponent
    lateinit var player: ThemedFrameLayout
    lateinit var playerImage: ImageView
    lateinit var cmptTitle: TitleUiComponent
    lateinit var cmptSeekbar: SeekbarUiComponent
    lateinit var cmptDashboard: DashboardUiComponent
    lateinit var cmptController: ControllerUiComponent
    lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
    lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            setBackgroundColor(Color.TRANSPARENT)
            isClickable = true

            view {
                setOnThemeChangeListener { theme ->
                    background = topShadow(theme)
                }
            }.lParams<_, frame_lp>(matchParent, dimens.shadowSize) {
                gravity = Gravity.TOP
            }

            cmptFlat = include<_, FlatUiComponent>(dimens.flatDimens!!)
                .lParams<_, frame_lp> {
                    gravity = Gravity.TOP
                    topMargin = dimens.shadowSize
                }

            player = frameLayout {
                setOnThemeChangeListener { theme ->
                    setBackgroundColor(Color.parseColor(theme.backgrounds))
                }
                alpha = 0f

                playerImage = imageView {
                    id = View.generateViewId()
                    setImageResource(R.drawable.ic_note)
                }.lParams<_, frame_lp>(dimens.imageSize, dimens.imageSize) {
                    gravity = Gravity.TOP or Gravity.CENTER
                }

                constraintLayout {
                    id = generateViewId()
                    setOnThemeChangeListener { theme ->
                        setBackgroundColor(Color.parseColor(theme.backgrounds))
                    }

                    cmptTitle = include<_, TitleUiComponent>(dimens.titleDimens) {
                        rootView.id = generateViewId()
                        tvTitle1.gravity = Gravity.CENTER
                        tvTitle2.gravity = Gravity.CENTER
                    }.lParams<_, constraint_lp> {
                        topToTop = this@constraintLayout.id
                        bottomToTop = getNextViewId()
                        rightToRight = this@constraintLayout.id
                        leftToLeft = this@constraintLayout.id
                    }

                    cmptSeekbar = include<_, SeekbarUiComponent>(dimens.seekbarDimens) {
                        rootView.id = generateViewId()
                    }.lParams<_, constraint_lp> {
                        topToBottom = cmptTitle.rootView.id
                        bottomToTop = getNextViewId()
                        rightToRight = this@constraintLayout.id
                        leftToLeft = this@constraintLayout.id
                    }

                    cmptDashboard = include<_, DashboardUiComponent>(dimens.dashboardDimens)
                        .lParams<_, constraint_lp> {
                            topToBottom = cmptSeekbar.rootView.id
                            bottomToTop = getNextViewId()
                            rightToRight = this@constraintLayout.id
                            leftToLeft = this@constraintLayout.id
                        }

                    cmptController = include<_, ControllerUiComponent>(dimens.controllerDimens) {
                        rootView.id = generateViewId()
                    }.lParams<_, constraint_lp> {
                        topToBottom = cmptDashboard.rootView.id
                        bottomToBottom = this@constraintLayout.id
                        rightToRight = this@constraintLayout.id
                        leftToLeft = this@constraintLayout.id

                        bottomMargin = dip(50)
                    }

                }.lParams<_, frame_lp>(matchParent, matchParent) {
                    topMargin = dimens.imageSize
                }

            }.lParams<_, frame_lp>(matchParent, matchParent)

        }
    }

    fun configBottomSheet(fm: FragmentManager) {
        metadataExtractor.initRetriever()
        cmptFlat.cmptFlatController.btPlay.setOnClickListener {
            mediaPlayerCore.playOrPause()
        }
        cmptFlat.cmptFlatController.btForward.setOnClickListener {
            mediaPlayerCore.playNext(byUser = true, repeat = false)
        }
        cmptFlat.cmptFlatController.btRewind.setOnClickListener {
            mediaPlayerCore.playPrevious()
        }
        cmptController.btPlay.setOnClickListener {
            mediaPlayerCore.playOrPause()
        }
        cmptController.btForward.setOnClickListener {
            mediaPlayerCore.playNext(byUser = true, repeat = false)
        }
        cmptController.btRewind.setOnClickListener {
            mediaPlayerCore.playPrevious()
        }
        cmptDashboard.repeat.setOnClickListener {
            mediaPlayerCore.changeRepeatState(
                when (mediaPlayerCore.repeatState) {
                    PlaybackStateCompat.REPEAT_MODE_NONE -> PlaybackStateCompat.REPEAT_MODE_ONE
                    PlaybackStateCompat.REPEAT_MODE_ONE -> PlaybackStateCompat.REPEAT_MODE_GROUP
                    PlaybackStateCompat.REPEAT_MODE_GROUP -> PlaybackStateCompat.REPEAT_MODE_ALL
                    else -> PlaybackStateCompat.REPEAT_MODE_NONE
                }
            ) {
                cmptDashboard.updateRepeatButton(mediaPlayerCore.repeatState)
            }
        }
        cmptDashboard.shuffle.setOnClickListener {
            mediaPlayerCore.changeShuffleState(
                when (mediaPlayerCore.shuffleState) {
                    PlaybackStateCompat.SHUFFLE_MODE_NONE -> PlaybackStateCompat.SHUFFLE_MODE_ALL
                    else -> PlaybackStateCompat.SHUFFLE_MODE_NONE
                }
            ) {
                cmptDashboard.updateShuffleButton(mediaPlayerCore.shuffleState)
            }
        }
        cmptDashboard.queue.setOnClickListener {
            DialogsManager.showDialogWithTag(fm, QueueDialog(), "queue")
        }
        cmptDashboard.favourite.setOnClickListener {
            mediaPlayerCore.getCurrentPlaying()?.also { music ->
                viewModel.addOrRemoveFromFavourites(music) {
                    cmptDashboard.updateFavouritesButton(music.favourite)
                    it.context.sendBroadCast(
                        Constants.BC_ACTION_REFRESH_PLAYLISTS,
                        Constants.BC_KEY_REFRESH_FAVOURITES
                    )
                }
            }
        }
        cmptSeekbar.onSeekBarChange { position ->
            mediaPlayerCore.seekTo(position)
        }
    }

    fun configBottomSheetBehavior(bottomSheet: FrameLayout) {
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        try {
            cmptFlat.rootView.updateLayoutParams<frame_lp> {
                sheetBehavior.peekHeight = height + topMargin
            }
        } catch (e: java.lang.Exception) {
            println("flat does not exist in xlarge screen mode")
        }
        cmptFlat.rootView.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    cmptFlat.rootView.visibility = View.GONE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (cmptFlat.rootView.visibility == View.GONE)
                    cmptFlat.rootView.visibility = View.VISIBLE
                cmptFlat.rootView.alpha = 1 - slideOffset
                player.alpha = slideOffset
            }
        }
        sheetBehavior.addBottomSheetCallback(bottomSheetCallback)
    }

    fun updateImagesAndLabels(context: Context, playingModel: com.saeedlotfi.limlam.domain.model.MusicDoModel) {
        cmptFlat.cmptFlatTitle.tvTitle1.text = playingModel.title
        cmptFlat.cmptFlatTitle.tvTitle2.text = context.getString(
            R.string.artist_album,
            playingModel.artist,
            playingModel.album
        )
        cmptFlat.cmptThumbnail.loadThumbnailFromCache(playingModel.id, R.drawable.ic_note)
        cmptTitle.tvTitle1.text = playingModel.title
        cmptTitle.tvTitle2.text = context.getString(
            R.string.artist_album,
            playingModel.artist,
            playingModel.album
        )
        metadataExtractor.extractPictureAsync(playingModel.path) { bmp ->
            if (bmp != null) playerImage.setImageBitmap(bmp)
            else playerImage.setImageResource(R.drawable.ic_note)
        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = displayMetrics.heightPixels,
            shadowSize = dip(15),
            flatDimens = FlatUiComponent.Dimens(
                width = matchParent,
                height = dip(70),
                strokeSize = 0,
                cornerRadius = 0,
                thumbnailDimens = ThumbnailUiComponent.Dimens(
                    width = dip(65),
                    height = dip(65)
                ),
                flatTitleDimens = TitleUiComponent.Dimens(
                    width = matchParent,
                    height = dip(65)
                ),
                flatControllerDimens = ControllerUiComponent.Dimens(
                    width = dip(120),
                    height = dip(40)
                )
            ),
            imageSize = displayMetrics.widthPixels,
            titleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(70)
            ),
            seekbarDimens = SeekbarUiComponent.Dimens(
                width = matchParent,
                height = dip(60)
            ),
            dashboardDimens = DashboardUiComponent.Dimens(
                width = matchParent,
                height = dip(50)
            ),
            controllerDimens = ControllerUiComponent.Dimens(
                width = dip(200),
                height = dip(60)
            ),
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        var shadowSize: Int,
        var flatDimens: FlatUiComponent.Dimens?,
        var imageSize: Int,
        var titleDimens: TitleUiComponent.Dimens,
        var seekbarDimens: SeekbarUiComponent.Dimens,
        var dashboardDimens: DashboardUiComponent.Dimens,
        var controllerDimens: ControllerUiComponent.Dimens,
    ) : BaseUiComponent.Dimens()

}


