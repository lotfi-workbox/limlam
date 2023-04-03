@file:Suppress("MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.userInterface.layouts

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface.layouts.PreviewerUiNormal.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.ControllerUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.SeekbarUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.ThumbnailUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.TitleUiComponent
import com.saeedlotfi.limlam.userInterface.viewModels.PreviewerViewModel
import javax.inject.Inject

open class PreviewerUiNormal @Inject constructor(
    protected open var mediaPlayerCore: MediaPlayerCore
) : BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout
    lateinit var viewModel: PreviewerViewModel

    lateinit var cmptThumbnail: ThumbnailUiComponent
    lateinit var cmptTitle: TitleUiComponent
    lateinit var cmptSeekbar: SeekbarUiComponent
    lateinit var cmptController: ControllerUiComponent

    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            setBackgroundColor(Color.TRANSPARENT)

            cardView {
                lParams<_, frame_lp>(dimens.width, dimens.height)
                setOnThemeChangeListener { theme ->
                    setCardBackgroundColor(Color.parseColor(theme.backgrounds))
                }
                radius = 20f

                view {
                    setOnThemeChangeListener { theme ->
                        setBackgroundColor(Color.parseColor(theme.topOfWindows))
                    }
                }.lParams<_, card_lp>(matchParent, dimens.thumbnailDimens.height)

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    cmptThumbnail = include<_, ThumbnailUiComponent>(dimens.thumbnailDimens)
                        .lParams<_, linear_lp> {
                            gravity = Gravity.CENTER
                        }

                    cmptTitle = include<_, TitleUiComponent>(dimens.titleDimens) {
                        tvTitle1.gravity = Gravity.CENTER
                        tvTitle2.gravity = Gravity.CENTER
                    }.lParams<_, linear_lp> {
                        leftMargin = dip(20)
                        rightMargin = dip(20)
                    }

                    cmptSeekbar = include<_, SeekbarUiComponent>(dimens.seekbarDimens)
                        .lParams<_, linear_lp> {
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                    cmptController = include<_, ControllerUiComponent>(dimens.controllerDimens)
                        .lParams<_, linear_lp> {
                            gravity = Gravity.CENTER
                            topMargin = dip(15)
                            bottomMargin = dip(15)
                        }

                }.lParams<_, card_lp>(matchParent, matchParent)

            }.lParams<_, frame_lp>(dimens.thumbnailDimens.width, wrapContent) {
                gravity = Gravity.CENTER
            }

        }
    }

    fun configUi() {
        cmptController.btPlay.setOnClickListener {
            mediaPlayerCore.playOrPause()
        }
        cmptController.btForward.setOnClickListener {
            mediaPlayerCore.playNext(repeat = false, byUser = true)
        }
        cmptController.btRewind.setOnClickListener {
            mediaPlayerCore.playPrevious()
        }
        cmptSeekbar.onSeekBarChange { position ->
            mediaPlayerCore.seekTo(position)
        }
    }

    fun updateImagesAndLabels(context: Context) {
        mediaPlayerCore.getCurrentPlaying()?.also { playingModel ->
            cmptTitle.tvTitle1.text = playingModel.title
            cmptTitle.tvTitle2.text = context.getString(
                R.string.artist_album, playingModel.artist, playingModel.album
            )
            cmptThumbnail.loadThumbnailFromCache(playingModel.id, R.drawable.ic_note)
        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = displayMetrics.widthPixels,
            height = displayMetrics.heightPixels,
            thumbnailDimens = ThumbnailUiComponent.Dimens(
                width = dip(300),
                height = dip(300),
            ),
            titleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(60)
            ),
            seekbarDimens = SeekbarUiComponent.Dimens(
                width = matchParent,
                height = dip(30)
            ),
            controllerDimens = ControllerUiComponent.Dimens(
                width = dip(150),
                height = dip(50)
            )
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        var thumbnailDimens: ThumbnailUiComponent.Dimens,
        var titleDimens: TitleUiComponent.Dimens,
        var seekbarDimens: SeekbarUiComponent.Dimens,
        var controllerDimens: ControllerUiComponent.Dimens
    ) : BaseUiComponent.Dimens()

}