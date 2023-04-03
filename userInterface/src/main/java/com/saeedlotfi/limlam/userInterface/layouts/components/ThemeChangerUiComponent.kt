package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageView
import com.saeedlotfi.limlam.userInterface.layouts.components.ThemeChangerUiComponent.*
import javax.inject.Inject
import kotlin.math.hypot

open class ThemeChangerUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedImageView, Dimens>() {

    override lateinit var rootView: ThemedImageView

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        imageView {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            visibility = View.GONE
        }
    }

    fun circularAnimation(
        capture: View, changer: View, onAnimation: () -> Unit
    ) = with(rootView) {
        changer.setOnClickListener {
            if (isVisible) return@setOnClickListener

            val w = capture.measuredWidth
            val h = capture.measuredHeight

            val bitmap =
                android.graphics.Bitmap.createBitmap(w, h, android.graphics.Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            capture.draw(canvas)

            setImageBitmap(bitmap)
            isVisible = true

            val finalRadius = hypot(w.toFloat(), h.toFloat())
            val anim = android.view.ViewAnimationUtils.createCircularReveal(
                this,
                changer.left + (changer.width / 2),
                changer.top + (changer.width / 2),
                finalRadius,
                0f
            )
            anim.duration = 600L
            anim.doOnStart {
                onAnimation()
            }
            anim.doOnEnd {
                setImageDrawable(null)
                isVisible = false
            }
            anim.start()
        }
    }

    override fun getDimens(uiContext: UiContext<Context>): Dimens = Dimens(
        width = wrapContent,
        height = wrapContent
    )

    class Dimens(override var width: Int, override var height: Int) : BaseUiComponent.Dimens()

}