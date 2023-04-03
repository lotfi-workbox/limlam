package com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.*
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvLinearFourPicUiNormal.*
import javax.inject.Inject

open class RvLinearFourPicUiNormal @Inject constructor() :
    BaseRvItemUiComponent<ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout
    override lateinit var mainLayout: ViewGroup

    lateinit var frameLayout: FrameLayout

    lateinit var cmptPicGroup: PicGroupUiComponent
    lateinit var cmptTitle: TitleUiComponent
    lateinit var cmptOption: IconUiComponent

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, frame_lp>(dimens.width, dimens.height)
            setPadding(dimens.strokeWidth)
            setOnThemeChangeListener { theme ->
                setBackgroundColor(Color.parseColor(theme.strokeColor))
            }

            frameLayout {
                mainLayout = this
                setOnThemeChangeListener { theme ->
                    background = Ripple.init(Color.parseColor(theme.backgrounds))
                }

                cmptPicGroup = include<_, PicGroupUiComponent>(dimens.picGroupDimens)
                    .lParams<_, frame_lp> {
                        gravity = Gravity.CENTER or Gravity.START
                        marginStart = dip(5)
                    }

                cmptTitle = include<_, TitleUiComponent>(dimens.cmptTitleDimens)
                    .lParams<_, frame_lp> {
                        gravity = Gravity.CENTER
                        marginStart = dimens.picGroupDimens.width
                        marginEnd = dimens.cmptOptionDimens.width
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
            picGroupDimens = PicGroupUiComponent.Dimens(
                width = dip(60),
                height = dip(60),
                thumbnailsDimens = ThumbnailUiComponent.Dimens(
                    width = dip(26),
                    height = dip(26)
                )
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

    class Dimens(
        override var width: Int,
        override var height: Int,
        var strokeWidth: Int,
        var picGroupDimens: PicGroupUiComponent.Dimens,
        var cmptTitleDimens: TitleUiComponent.Dimens,
        var cmptOptionDimens: IconUiComponent.Dimens,
    ) : BaseUiComponent.Dimens()

}

class RvLinearFourPicUiLarge @Inject constructor() : RvLinearFourPicUiNormal() {

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = dip(115),
            strokeWidth = dip(2),
            picGroupDimens = PicGroupUiComponent.Dimens(
                width = dip(102),
                height = dip(102),
                thumbnailsDimens = ThumbnailUiComponent.Dimens(
                    width = dip(26),
                    height = dip(26)
                )
            ),
            cmptTitleDimens = TitleUiComponent.Dimens(
                width = matchParent,
                height = dip(60)
            ),
            cmptOptionDimens = IconUiComponent.Dimens(
                width = dip(30),
                height = dip(30)
            )
        )
    }

}