package com.saeedlotfi.limlam.userInterface.layouts._commom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

object Ripple {

    fun init(mask: Drawable? = null): Drawable? {
        return try {
            val color = Color.parseColor(ThemeManager.theme.rippleColor)
            RippleDrawable(ColorStateList.valueOf(color), null, mask)
        } catch (e: Exception) {
            null
        }
    }

    fun init(context: Context, @DrawableRes id: Int): LayerDrawable {
        return LayerDrawable(
            arrayOf(
                ContextCompat.getDrawable(context, id)?.also { drawable ->
                    DrawableCompat.setTint(
                        drawable,
                        Color.parseColor(ThemeManager.theme.textsAndIcons)
                    )
                },
                init()
            )
        )
    }

    fun init(maskColor: Int): Drawable {
        return LayerDrawable(
            arrayOf(
                ColorDrawable(maskColor),
                init()
            )
        )
    }

}