package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.SeekBar
import android.widget.TextView
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface._common.createTimeLabel
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout
import com.saeedlotfi.limlam.userInterface.layouts.components.SeekbarUiComponent.*
import javax.inject.Inject

open class SeekbarUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedFrameLayout, Dimens>() {

    override lateinit var rootView: ThemedFrameLayout

    lateinit var seekBar: SeekBar
    lateinit var elapsedTime: TextView
    lateinit var remainingTime: TextView

    private var runnable: Runnable? = null
    private var thread: Thread? = null
    private var handler: Handler? = null

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        frameLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            setBackgroundColor(Color.TRANSPARENT)

            textView {
                elapsedTime = this
                setOnThemeChangeListener { theme ->
                    setTextColor(Color.parseColor(theme.textsAndIcons))
                }
                text = context.getString(R.string.empty_time)
                textSize = 12f
                gravity = Gravity.CENTER
            }.lParams<_, frame_lp>(dimens.height, dimens.height) {
                gravity = Gravity.START
            }

            seekBar {
                seekBar = this
                setOnThemeChangeListener { theme ->
                    progressTintList =
                        ColorStateList.valueOf(Color.parseColor(theme.textsAndIcons))
                    thumbTintList =
                        ColorStateList.valueOf(Color.parseColor(theme.textsAndIcons))
                    progressBackgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(theme.rippleColor))
                }
            }.lParams<_, frame_lp>(matchParent, dimens.height) {
                gravity = Gravity.CENTER or Gravity.TOP
                marginStart = dimens.height
                marginEnd = dimens.height
            }

            textView {
                remainingTime = this
                setOnThemeChangeListener { theme ->
                    setTextColor(Color.parseColor(theme.textsAndIcons))
                }
                textSize = 12f
                text = context.getString(R.string.empty_time)
                gravity = Gravity.CENTER
            }.lParams<_, frame_lp>(dimens.height, dimens.height) {
                gravity = Gravity.END
            }

        }
    }

    fun updateSeekbar(duration: Int, currentPosition: Int) {
        seekBar.max = duration
        seekBar.progress = currentPosition
        remainingTime.post {
            remainingTime.text = (duration - currentPosition).createTimeLabel()
        }
        elapsedTime.post {
            elapsedTime.text = currentPosition.createTimeLabel()
        }
    }

    fun startUpdateSeekbar(duration: Int, currentPosition: Int) {
        var position = currentPosition

        thread?.interrupt()

        if (runnable != null) handler?.removeCallbacks(runnable!!)

        if (handler == null) handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            override fun run() {
                updateSeekbar(duration, position)
                position += 1000
                handler?.postDelayed(this, 1000)
            }
        }

        thread = Thread(runnable!!)

        thread?.start()
    }

    fun stopUpdateSeekbar() {
        thread?.interrupt()
        if (runnable != null) handler?.removeCallbacks(runnable!!)
    }

    fun onSeekBarChange(onChange: (position: Int) -> Unit) {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                var finalPosition: Int = 0
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        finalPosition = progress
                        elapsedTime.text = progress.createTimeLabel()
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    onChange(finalPosition)
                }
            }
        )
    }

    override fun getDimens(uiContext: UiContext<Context>): Dimens = Dimens(
        width = wrapContent,
        height = wrapContent
    )

    class Dimens(override var width: Int, override var height: Int) : BaseUiComponent.Dimens()

}