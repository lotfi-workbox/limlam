package com.saeedlotfi.limlam.userInterface.layouts.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.RvListUiComponent.*
import javax.inject.Inject

class RvListUiComponent @Inject constructor() : BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var rvList: RecyclerView
    lateinit var tvEmpty: TextView
    lateinit var flProgressBar: FrameLayout

    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {

        frameLayout {

            textView {
                tvEmpty = this
                setTextColor(Color.parseColor(ThemeManager.theme.textsAndIcons))
                visibility = View.GONE
                textSize = 20f
                text = ctx.getString(R.string.empty)
            }.lParams<_, frame_lp> {
                gravity = Gravity.TOP or Gravity.CENTER
                topMargin = dip(150)
            }

            frameLayout {
                flProgressBar = this
                visibility = View.GONE

                progressBar {
                    setOnThemeChangeListener { theme ->
                        indeterminateTintList =
                            ColorStateList.valueOf(Color.parseColor(theme.textsAndIcons))
                    }
                }.lParams<_, frame_lp>(dip(100), dip(100)) {
                    gravity = Gravity.TOP
                }

                textView {
                    setTextColor(Color.parseColor(ThemeManager.theme.textsAndIcons))
                    text = ctx.getString(R.string.loading)
                }.lParams<_, frame_lp>(wrapContent, dip(30)) {
                    gravity = Gravity.BOTTOM
                }

            }.lParams<_, frame_lp>(wrapContent, dip(140)) {
                gravity = Gravity.CENTER
            }

            recyclerView {
                rvList = this
            }.lParams<_, frame_lp>(matchParent, matchParent)

        }

    }

    override fun getDimens(uiContext: UiContext<Context>): Dimens = Dimens()

    class Dimens : BaseUiComponent.Dimens()

}