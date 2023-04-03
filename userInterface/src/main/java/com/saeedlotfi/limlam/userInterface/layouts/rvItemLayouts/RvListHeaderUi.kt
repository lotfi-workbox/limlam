package com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.RoundImageView
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedCardView
import com.saeedlotfi.limlam.userInterface.layouts.components.BaseRvItemUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.ThumbnailUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.TitleUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvListHeaderUiNormal.*
import javax.inject.Inject

open class RvListHeaderUiNormal @Inject constructor() :
    BaseRvItemUiComponent<ThemedCardView, Dimens>() {

    override lateinit var rootView: ThemedCardView
    override lateinit var mainLayout: ViewGroup

    lateinit var cmptThumbnail: ThumbnailUiComponent
    lateinit var cmptTitle: TitleUiComponent

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        cardView {
            lParams<_, card_lp>(dimens.width, dimens.height)
            setOnThemeChangeListener { theme ->
                setCardBackgroundColor(Color.parseColor(theme.backgrounds))
            }
            setPadding(dimens.strokeWidth)
            cardElevation = dimens.elevationSize

            frameLayout {
                mainLayout = this
                setOnThemeChangeListener { theme ->
                    background = Ripple.init(Color.parseColor(theme.backgrounds))
                }
                isClickable = true

                cmptThumbnail = include<_, ThumbnailUiComponent>(dimens.thumbnailDimens) {
                    rootView.setMode(RoundImageView.ImageViewMode.Round)
                }.lParams<_, frame_lp> {
                    gravity = Gravity.START
                    marginStart = dip(5)
                }

                cmptTitle = include<_, TitleUiComponent>(dimens.cmptTitleDimens)
                    .lParams<_, frame_lp> {
                        gravity = Gravity.CENTER
                        marginStart = dimens.thumbnailDimens.width
                    }

            }.lParams<_, card_lp>(matchParent, matchParent)

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = dip(90),
            strokeWidth = dip(1),
            elevationSize = dip(10).toFloat(),
            thumbnailDimens = ThumbnailUiComponent.Dimens(
                width = dip(90),
                height = dip(90)
            ),
            cmptTitleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(60)
            )
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        var strokeWidth: Int,
        var elevationSize: Float,
        var thumbnailDimens: ThumbnailUiComponent.Dimens,
        var cmptTitleDimens: TitleUiComponent.Dimens
    ) : BaseUiComponent.Dimens()

}

class RvListHeaderUiLarge @Inject constructor() : RvListHeaderUiNormal() {

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = dip(120),
            strokeWidth = dip(2),
            elevationSize = dip(10).toFloat(),
            thumbnailDimens = ThumbnailUiComponent.Dimens(
                width = dip(120),
                height = dip(120)
            ),
            cmptTitleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(80)
            )
        )
    }

}