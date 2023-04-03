package com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.BaseRvItemUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.IconUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.ThumbnailUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.TitleUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvLinearOnePicUiNormal.*
import javax.inject.Inject

open class RvLinearOnePicUiNormal @Inject constructor() :
    BaseRvItemUiComponent<ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout
    override lateinit var mainLayout: ViewGroup

    lateinit var cmptThumbnail: ThumbnailUiComponent
    lateinit var cmptTitle: TitleUiComponent
    lateinit var cmptOption: IconUiComponent

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, frame_lp>(dimens.width, dimens.height)
            setOnThemeChangeListener { theme ->
                setBackgroundColor(Color.parseColor(theme.strokeColor))
            }
            setPadding(dimens.strokeWidth)

            frameLayout {
                mainLayout = this
                setOnThemeChangeListener { theme ->
                    background = Ripple.init(Color.parseColor(theme.backgrounds))
                }

                cmptThumbnail = include<_, ThumbnailUiComponent>(dimens.thumbnailDimens)
                    .lParams<_, frame_lp> {
                        gravity = Gravity.CENTER or Gravity.START
                        marginStart = dip(5)
                    }

                cmptTitle = include<_, TitleUiComponent>(dimens.cmptTitleDimens)
                    .lParams<_, frame_lp> {
                        gravity = Gravity.CENTER
                        marginStart = dimens.thumbnailDimens.width
                        marginEnd = dimens.cmptOptionDimens.width + dip(10)
                    }

                cmptOption = include<_, IconUiComponent>(dimens.cmptOptionDimens) {
                    setImageResource(R.drawable.ic_option)
                }.lParams<_, frame_lp> {
                    gravity = Gravity.CENTER or Gravity.END
                }

            }.lParams<_, frame_lp>(matchParent, matchParent)

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = dip(70),
            strokeWidth = dip(1),
            thumbnailDimens = ThumbnailUiComponent.Dimens(
                width = dip(60),
                height = dip(60)
            ),
            cmptTitleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(50)
            ),
            cmptOptionDimens = IconUiComponent.Dimens(
                width = dip(30),
                height = dip(30)
            )
        )
    }

    open class Dimens(
        override var width: Int,
        override var height: Int,
        open var strokeWidth: Int,
        open var thumbnailDimens: ThumbnailUiComponent.Dimens,
        open var cmptTitleDimens: TitleUiComponent.Dimens,
        open var cmptOptionDimens: IconUiComponent.Dimens,
    ) : BaseUiComponent.Dimens()

}

class RvLinearOnePicUiLarge @Inject constructor() : RvLinearOnePicUiNormal() {

    override fun getDimens(uiContext: UiContext<Context>): Dimens = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = dip(110),
            strokeWidth = dip(2),
            thumbnailDimens = ThumbnailUiComponent.Dimens(
                width = dip(60),
                height = dip(60)
            ),
            cmptTitleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(60)
            ),
            cmptOptionDimens = IconUiComponent.Dimens(
                width = dip(40),
                height = dip(40)
            )
        )
    }

}