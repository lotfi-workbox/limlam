package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageButton
import com.saeedlotfi.limlam.userInterface.layouts.components.IconUiComponent.*
import javax.inject.Inject

open class IconUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedImageButton, Dimens>() {

    override lateinit var rootView: ThemedImageButton

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        imageButton {
            setOnThemeChangeListener { theme ->
                setColorFilter(
                    Color.parseColor(theme.textsAndIcons),
                    PorterDuff.Mode.SRC_IN
                )
            }
            background = Ripple.init()
        }.lParams<_, vGroup_lp>(dimens.width, dimens.height)
    }

    fun setImageResource(@DrawableRes id: Int) = with(rootView) {
        post {
            try {
                setImageBitmap(
                    ContextCompat.getDrawable(context, id)?.mutate()
                        ?.toBitmap(width - dip(5), height - dip(5))
                )
            } catch (e: java.lang.Exception) {
                println(e.message)
            }
        }
    }

    override fun getDimens(uiContext: UiContext<Context>): Dimens = Dimens(
        width = wrapContent,
        height = wrapContent
    )

    class Dimens(override var width: Int, override var height: Int) : BaseUiComponent.Dimens()

}