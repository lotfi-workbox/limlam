package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Gravity
import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageButton
import com.saeedlotfi.limlam.userInterface.layouts.components.ControllerUiComponent.*
import javax.inject.Inject

open class ControllerUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var btPlay: ThemedImageButton
    lateinit var btForward: ImageButton
    lateinit var btRewind: ImageButton

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            setBackgroundColor(Color.TRANSPARENT)

            imageButton {
                btRewind = this
                background = Ripple.init()
                setOnThemeChangeListener { theme ->
                    setColorFilter(
                        Color.parseColor(theme.textsAndIcons),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                ContextCompat.getDrawable(ctx, R.drawable.ic_rewind)?.also { drawable ->
                    setImageBitmap(
                        drawable.mutate().toBitmap(
                            dimens.height - dip(10), dimens.height - dip(10)
                        )
                    )
                }
            }.lParams<_, frame_lp>(dimens.height, dimens.height) {
                gravity = Gravity.CENTER or Gravity.START
            }

            imageButton {
                btPlay = this
                background = Ripple.init()
                setOnThemeChangeListener { theme ->
                    setColorFilter(
                        Color.parseColor(theme.textsAndIcons),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                ContextCompat.getDrawable(ctx, R.drawable.ic_play)?.also { drawable ->
                    setImageBitmap(
                        drawable.mutate().toBitmap(
                            dimens.height - dip(5), dimens.height - dip(5)
                        )
                    )
                }
            }.lParams<_, frame_lp>(dimens.height, dimens.height) {
                gravity = Gravity.CENTER
            }

            imageButton {
                btForward = this
                background = Ripple.init()
                setOnThemeChangeListener { theme ->
                    setColorFilter(
                        Color.parseColor(theme.textsAndIcons),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                ContextCompat.getDrawable(ctx, R.drawable.ic_forward)?.also { drawable ->
                    setImageBitmap(
                        drawable.mutate().toBitmap(
                            dimens.height - dip(10), dimens.height - dip(10)
                        )
                    )
                }
            }.lParams<_, frame_lp>(dimens.height, dimens.height) {
                gravity = Gravity.CENTER or Gravity.END
            }

        }
    }

    fun updatePlayButton(isPlaying: Boolean) = with(btPlay) {
        val drawableId = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        post {
            try {
                setImageBitmap(
                    AppCompatResources.getDrawable(context, drawableId)?.mutate()
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