package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.TitleUiComponent.*
import javax.inject.Inject

open class TitleUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var tvTitle1: TextView
    lateinit var tvTitle2: TextView

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            setBackgroundColor(Color.TRANSPARENT)

            textView {
                tvTitle1 = this
                setOnThemeChangeListener { theme ->
                    setTextColor(Color.parseColor(theme.textsAndIcons))
                }
                text = ctx.getString(R.string.unknown)
                gravity = Gravity.CENTER or Gravity.LEFT
                ellipsize = TextUtils.TruncateAt.END
                isSingleLine = true
                textSize = 12f
            }.lParams<_, frame_lp>(matchParent, dimens.height / 2) {
                gravity = Gravity.TOP
            }


            textView {
                tvTitle2 = this
                setOnThemeChangeListener { theme ->
                    setTextColor(Color.parseColor(theme.textsAndIcons))
                }
                text = ctx.getString(R.string.unknown)
                gravity = Gravity.CENTER or Gravity.LEFT
                ellipsize = TextUtils.TruncateAt.END
                isSingleLine = true
                textSize = 12f
            }.lParams<_, frame_lp>(matchParent, dimens.height / 2) {
                gravity = Gravity.BOTTOM
            }

        }
    }

    fun justOneTitle() {
        tvTitle2.visibility = View.GONE
        tvTitle1.updateLayoutParams<frame_lp> { gravity = Gravity.CENTER }
    }

    override fun getDimens(uiContext: UiContext<Context>): Dimens = Dimens(
        width = wrapContent,
        height = wrapContent
    )

    class Dimens(override var width: Int, override var height: Int) : BaseUiComponent.Dimens()
}