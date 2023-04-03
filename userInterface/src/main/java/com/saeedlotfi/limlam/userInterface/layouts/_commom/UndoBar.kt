@file:Suppress("unused")

package com.saeedlotfi.limlam.userInterface.layouts._commom

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class UndoBar {

    private var message: String = ""
    private var actionText: String = ""
    private var viewGroup: ViewGroup? = null
    private var onDismissDo : () -> Unit = {}

    fun init(message: String,actionText: String, viewGroup: ViewGroup) : UndoBar {
        this.message = message
        this.actionText = actionText
        this.viewGroup = viewGroup
        return this
    }

    fun onDismissListener(onDismissDo : () -> Unit) : UndoBar {
        this.onDismissDo = onDismissDo
        return this
    }

    fun show(context: Context) {
        viewGroup?.also{ viewGroup ->
            val snackBar = Snackbar.make(viewGroup, message, 3000)
            snackBar.view.background = GradientDrawable().apply {
                setColor(Color.parseColor(ThemeManager.theme.undoBar))
                alpha = 240
                cornerRadius = 10f
            }

            (snackBar.view.layoutParams as FrameLayout.LayoutParams).also {
                it.height = dip(50f,context)
                it.gravity = Gravity.BOTTOM or Gravity.CENTER
                it.setMargins( dip(10f,context),0, dip(10f,context), 0)
            }
            snackBar.view.translationY = -dip(50f,context).toFloat()
            snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
                setTextColor(Color.WHITE)
                textSize = 15.0f
            }
            snackBar.view.alpha = 0f
            var canceled = false
            snackBar.setAction("Cancel") {
                canceled = true
                snackBar.dismiss()
            }
            snackBar.setActionTextColor(Color.rgb(68,196,161))
            snackBar.addCallback(object : Snackbar.Callback() {
                override fun onShown(snackbar: Snackbar?) {
                    snackbar?.view?.animate()?.alpha(1f)?.duration = 400
                    @Suppress("DEPRECATION")
                    Handler().postDelayed({
                        if (!canceled){
                            onDismissDo()
                        }
                        snackBar.dismiss()
                    }, 3000)
                }
            })
            snackBar.show()
        }
    }

    private fun dip(dp: Float, context: Context?): Int {
        return if (context != null) {
            val resources = context.resources
            val metrics = resources.displayMetrics
            (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
        } else {
            val metrics = Resources.getSystem().displayMetrics
            (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
        }
    }

}