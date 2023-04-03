package com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedTextView
import com.saeedlotfi.limlam.userInterface.layouts.components.BaseRvItemUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.components.ThumbnailUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvGridUiNormal.*
import javax.inject.Inject

class RvGridUiNormal @Inject constructor() : BaseRvItemUiComponent<ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout
    override lateinit var mainLayout: ViewGroup

    lateinit var cmptThumbnail: ThumbnailUiComponent
    lateinit var textView1: ThemedTextView

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {

        val width = displayMetrics.widthPixels
        val density = (displayMetrics.widthPixels / displayMetrics.density).toInt()

        frameLayout {
            lParams<_, frame_lp>(width / ((density / 100)), width / ((density / 100)))
            setPadding(dip(1), dip(1), dip(1), dip(1))
            setOnThemeChangeListener { theme ->
                setBackgroundColor(Color.parseColor(theme.strokeColor))
            }

            frameLayout {
                mainLayout = this
                setOnThemeChangeListener { theme ->
                    @Suppress("DEPRECATION")
                    setBackgroundDrawable(Ripple.init(Color.parseColor(theme.backgrounds)))
                }

                cmptThumbnail = include<_, ThumbnailUiComponent>(dimens.thumbnailsDimens)
                    .lParams<_, frame_lp> {
                        gravity = Gravity.TOP or Gravity.CENTER
                        topMargin = dip(10)
                    }

                textView {
                    textView1 = this
                    setOnThemeChangeListener { theme ->
                        setTextColor(Color.parseColor(theme.textsAndIcons))
                    }
                    gravity = Gravity.CENTER
                    ellipsize = TextUtils.TruncateAt.END
                    isSingleLine = true
                    textSize = 11f
                }.lParams<_, frame_lp>(matchParent, dip(15)) {
                    gravity = Gravity.BOTTOM or Gravity.CENTER
                    bottomMargin = dip(10)
                    leftMargin = dip(10)
                    rightMargin = dip(10)
                }

            }.lParams<_, frame_lp>(matchParent, matchParent)

        }

    }

    override fun getDimens(uiContext: UiContext<Context>) = with(uiContext) {
        return@with Dimens(
            thumbnailsDimens = ThumbnailUiComponent.Dimens(
                width = dip(70),
                height = dip(70)
            )
        )
    }

    class Dimens(
        var thumbnailsDimens: ThumbnailUiComponent.Dimens
    ) : BaseUiComponent.Dimens()

}

