package com.saeedlotfi.limlam.userInterface.layouts.bottomSheetUi

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface._common.MusicMetadataExtractor
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts.components.ControllerUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.DashboardUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.SeekbarUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.TitleUiComponent
import javax.inject.Inject

class BottomSheetUiSplitL @Inject constructor(
    override var mediaPlayerCore: MediaPlayerCore,
    override var metadataExtractor: MusicMetadataExtractor
) : BottomSheetUiNormal(mediaPlayerCore, metadataExtractor) {

    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            setBackgroundColor(Color.TRANSPARENT)
            isClickable = true

            //playerUi
            player = frameLayout {
                setOnThemeChangeListener { theme ->
                    setBackgroundColor(Color.parseColor(theme.backgrounds))
                }

                playerImage = imageView {
                    setImageResource(R.drawable.ic_note)
                }.lParams<_, frame_lp>(dimens.imageSize, dimens.imageSize) {
                    gravity = Gravity.START or Gravity.CENTER
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
                        leftToLeft = this@constraintLayout.id
                        rightToRight = this@constraintLayout.id
                    }

                    cmptSeekbar = include<_, SeekbarUiComponent>(dimens.seekbarDimens) {
                        rootView.id = generateViewId()
                    }.lParams<_, constraint_lp> {
                        topToBottom = cmptTitle.rootView.id
                        bottomToTop = getNextViewId()
                        leftToLeft = this@constraintLayout.id
                        rightToRight = this@constraintLayout.id
                    }

                    cmptDashboard = include<_, DashboardUiComponent>(dimens.dashboardDimens)
                        .lParams<_, constraint_lp> {
                            topToBottom = cmptSeekbar.rootView.id
                            bottomToTop = getNextViewId()
                            leftToLeft = this@constraintLayout.id
                            rightToRight = this@constraintLayout.id
                        }

                    cmptController = include<_, ControllerUiComponent>(dimens.controllerDimens) {
                        rootView.id = generateViewId()
                    }.lParams<_, constraint_lp> {
                        topToBottom = cmptDashboard.rootView.id
                        bottomToBottom = this@constraintLayout.id
                        leftToLeft = this@constraintLayout.id
                        rightToRight = this@constraintLayout.id
                    }

                }.lParams<_, frame_lp>(matchParent, matchParent) {
                    marginStart = dimens.imageSize
                    bottomMargin = dip(25)
                }

            }.lParams<_, frame_lp>(matchParent, matchParent) {
                gravity = Gravity.BOTTOM or Gravity.CENTER
            }

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = displayMetrics.widthPixels,
            height = displayMetrics.heightPixels,
            shadowSize = dip(15),
            flatDimens = null,
            imageSize = displayMetrics.heightPixels / 2,
            titleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(70)
            ),
            seekbarDimens = SeekbarUiComponent.Dimens(
                width = matchParent,
                height = dip(40)
            ),
            dashboardDimens = DashboardUiComponent.Dimens(
                width = matchParent,
                height = dip(40)
            ),
            controllerDimens = ControllerUiComponent.Dimens(
                width = dip(150),
                height = dip(50)
            ),
        )
    }


}