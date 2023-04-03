package com.saeedlotfi.limlam.userInterface.layouts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts.RvListDialogUiNormal.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import javax.inject.Inject

open class RvListDialogUiNormal @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var rvList: RecyclerView
    lateinit var btAccept: TextView

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)

            cardView {
                useCompatPadding = true
                setOnThemeChangeListener { theme ->
                    setCardBackgroundColor(Color.parseColor(theme.paleBackgrounds))
                }
                setPadding(dip(10))
                setContentPadding(dip(12))
                radius = 20f
                cardElevation = dip(15).toFloat()

                rvList = recyclerView {
                    setOnThemeChangeListener { theme ->
                        setBackgroundColor(Color.parseColor(theme.windowBackground))
                    }
                    setPadding(dip(1))
                }.lParams<_, card_lp>(matchParent, matchParent) {
                    bottomMargin = dip(45)
                }

                btAccept = textView {
                    setOnThemeChangeListener { theme ->
                        setTextColor(Color.parseColor(theme.textsAndIcons))
                    }
                    background = Ripple.init()
                    text = ctx.getString(R.string.accept)
                    gravity = Gravity.CENTER
                    setPadding(dip(10))
                }.lParams<_, card_lp>(wrapContent, dip(40)) {
                    gravity = Gravity.BOTTOM or Gravity.RIGHT
                    rightMargin = dip(10)
                }

            }.lParams<_, frame_lp>(dimens.width, dimens.height - dip(50))

        }
    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            width = displayMetrics.widthPixels,
            height = displayMetrics.heightPixels
        )
    }

    class Dimens(override var width: Int, override var height: Int) : BaseUiComponent.Dimens()

}
