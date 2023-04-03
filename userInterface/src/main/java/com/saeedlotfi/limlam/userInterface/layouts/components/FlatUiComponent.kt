package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.FlatUiComponent.*
import javax.inject.Inject

open class FlatUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var cmptThumbnail: ThumbnailUiComponent
    lateinit var cmptFlatTitle: TitleUiComponent
    lateinit var cmptFlatController: ControllerUiComponent

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            setOnThemeChangeListener { theme ->
                background = GradientDrawable().also { gd ->
                    gd.shape = GradientDrawable.RECTANGLE
                    gd.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                    gd.color = ColorStateList.valueOf(Color.parseColor(theme.backgrounds))
                    gd.setStroke(dimens.strokeSize, Color.parseColor(theme.textsAndIcons))
                    gd.cornerRadius = dimens.cornerRadius.toFloat()
                }
            }

            cmptThumbnail = include<_, ThumbnailUiComponent>(dimens.thumbnailDimens) {
                setImageResource(R.drawable.ic_note)
                rootView.invalidate()
            }.lParams<_, frame_lp> {
                gravity = Gravity.CENTER or Gravity.START
                topMargin = dip(1)
                marginStart = dip(3)
            }

            cmptFlatTitle = include<_, TitleUiComponent>(dimens.flatTitleDimens)
                .lParams<_, frame_lp> {
                    marginStart = dimens.thumbnailDimens.width + dip(10)
                    marginEnd = dimens.flatControllerDimens.width + dip(5)
                }

            cmptFlatController = include<_, ControllerUiComponent>(dimens.flatControllerDimens)
                .lParams<_, frame_lp> {
                    gravity = Gravity.CENTER or Gravity.END
                }

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = wrapContent,
            height = wrapContent,
            strokeSize = dip(2),
            cornerRadius = dip(10),
            thumbnailDimens = ThumbnailUiComponent.Dimens(
                width = dip(50),
                height = dip(50)
            ),
            flatTitleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = matchParent
            ),
            flatControllerDimens = ControllerUiComponent.Dimens(
                width = dip(120),
                height = dip(40)
            )
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        val strokeSize: Int,
        val cornerRadius: Int,
        val thumbnailDimens: ThumbnailUiComponent.Dimens,
        var flatTitleDimens: TitleUiComponent.Dimens,
        var flatControllerDimens: ControllerUiComponent.Dimens,
    ) : BaseUiComponent.Dimens()

}