package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.PicGroupUiComponent.*
import javax.inject.Inject

open class PicGroupUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var cmptThumbnail: ThumbnailUiComponent
    lateinit var cmptThumbnail1: ThumbnailUiComponent
    lateinit var cmptThumbnail2: ThumbnailUiComponent
    lateinit var cmptThumbnail3: ThumbnailUiComponent

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, constraint_lp>(dimens.width, dimens.height)
            setOnThemeChangeListener { theme ->
                background = GradientDrawable().also { gd ->
                    gd.shape = GradientDrawable.RECTANGLE
                    gd.color =
                        ColorStateList.valueOf(Color.parseColor(theme.paleBackgrounds))
                    gd.setStroke(dip(1), Color.parseColor(theme.textsAndIcons))
                    gd.cornerRadius = dip(20).toFloat()
                }
            }

            cmptThumbnail = include<_, ThumbnailUiComponent>(dimens.thumbnailsDimens)
                .lParams<_, frame_lp> {
                    gravity = Gravity.TOP or Gravity.LEFT
                    topMargin = dip(4)
                    leftMargin = dip(4)
                }

            cmptThumbnail1 = include<_, ThumbnailUiComponent>(dimens.thumbnailsDimens)
                .lParams<_, frame_lp> {
                    gravity = Gravity.TOP or Gravity.RIGHT
                    topMargin = dip(4)
                    rightMargin = dip(4)
                }

            cmptThumbnail2 = include<_, ThumbnailUiComponent>(dimens.thumbnailsDimens)
                .lParams<_, frame_lp> {
                    gravity = Gravity.BOTTOM or Gravity.LEFT
                    bottomMargin = dip(4)
                    leftMargin = dip(4)
                }

            cmptThumbnail3 = include<_, ThumbnailUiComponent>(dimens.thumbnailsDimens)
                .lParams<_, frame_lp> {
                    gravity = Gravity.BOTTOM or Gravity.RIGHT
                    bottomMargin = dip(4)
                    rightMargin = dip(4)
                }

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = wrapContent,
            height = wrapContent,
            thumbnailsDimens = ThumbnailUiComponent.Dimens(
                width = dip(26),
                height = dip(26)
            )
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        var thumbnailsDimens: ThumbnailUiComponent.Dimens
    ) : BaseUiComponent.Dimens()

}