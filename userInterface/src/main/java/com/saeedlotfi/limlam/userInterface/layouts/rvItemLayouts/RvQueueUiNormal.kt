package com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.IconUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvQueueUiNormal.*
import javax.inject.Inject

open class RvQueueUiNormal @Inject constructor(
    private val mainLayout: UiComponentFactory<RvLinearOnePicUiNormal>
) : BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var cmtDrag: IconUiComponent
    lateinit var cmptMain: RvLinearOnePicUiNormal

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, card_lp>(dimens.width, dimens.height)
            setOnThemeChangeListener { theme ->
                setBackgroundColor(Color.parseColor(theme.backgrounds))
            }

            cmtDrag = include<_, IconUiComponent>(dimens.cmptDragDimens) {
                setImageResource(R.drawable.ic_equals)
            }.lParams<_, frame_lp> {
                gravity = Gravity.CENTER or Gravity.START
            }

            cmptMain = include(mainLayout).lParams<_, frame_lp> {
                marginStart = dimens.cmptDragDimens.width
            }

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = matchParent,
            height = wrapContent,
            cmptDragDimens = IconUiComponent.Dimens(
                width = dip(40),
                height = dip(40)
            )
        )
    }

    class Dimens(
        override var width: Int,
        override var height: Int,
        var cmptDragDimens: IconUiComponent.Dimens
    ) : BaseUiComponent.Dimens()

}