package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.layouts._commom.Ripple
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedConstraintLayout
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageButton
import com.saeedlotfi.limlam.userInterface.layouts.components.DashboardUiComponent.*
import javax.inject.Inject

open class DashboardUiComponent @Inject constructor() :
    BaseUiComponent<Context, ThemedConstraintLayout, Dimens>() {

    override lateinit var rootView: ThemedConstraintLayout

    lateinit var repeat: ThemedImageButton
    lateinit var shuffle: ThemedImageButton
    lateinit var queue: ThemedImageButton
    lateinit var favourite: ThemedImageButton

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        constraintLayout {
            lParams<_, vGroup_lp>(dimens.width, dimens.height)
            id = generateViewId()
            setBackgroundColor(Color.TRANSPARENT)

            queue = imageButton {
                id = generateViewId()
                setOnThemeChangeListener { theme ->
                    setColorFilter(
                        Color.parseColor(theme.textsAndIcons),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                background = Ripple.init()
                ContextCompat.getDrawable(ctx, R.drawable.ic_queue)?.also { drawable ->
                    setImageBitmap(drawable.mutate().toBitmap(dip(20), dip(20)))
                }
            }.lParams<_, constraint_lp>(dimens.height, dimens.height) {
                rightToRight = this@constraintLayout.id
                leftToRight = getNextViewId()
            }

            favourite = imageButton {
                id = generateViewId()
                setOnThemeChangeListener { theme ->
                    setColorFilter(
                        Color.parseColor(theme.textsAndIcons),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                background = Ripple.init()
                ContextCompat.getDrawable(ctx, R.drawable.ic_favourite)?.also { drawable ->
                    setImageBitmap(drawable.mutate().toBitmap(dip(20), dip(20)))
                }
            }.lParams<_, constraint_lp>(dimens.height, dimens.height) {
                rightToLeft = queue.id
                leftToRight = getNextViewId()
            }

            repeat = imageButton {
                id = generateViewId()
                setOnThemeChangeListener { theme ->
                    setColorFilter(
                        Color.parseColor(theme.textsAndIcons),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                background = Ripple.init()
                ContextCompat.getDrawable(ctx, R.drawable.ic_repeat_one)?.also { drawable ->
                    setImageBitmap(drawable.mutate().toBitmap(dip(20), dip(20)))
                }
            }.lParams<_, constraint_lp>(dimens.height, dimens.height) {
                rightToLeft = favourite.id
                leftToRight = getNextViewId()
            }

            shuffle = imageButton {
                id = generateViewId()
                setOnThemeChangeListener { theme ->
                    setColorFilter(
                        Color.parseColor(theme.textsAndIcons),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                background = Ripple.init()
                ContextCompat.getDrawable(ctx, R.drawable.ic_random)?.also { drawable ->
                    setImageBitmap(drawable.mutate().toBitmap(dip(20), dip(20)))
                }
            }.lParams<_, constraint_lp>(dimens.height, dimens.height) {
                rightToLeft = repeat.id
                leftToLeft = this@constraintLayout.id
            }

        }
    }

    fun updateFavouritesButton(isFavourites: Boolean) = with(favourite) {
        val id = if (isFavourites) R.drawable.ic_is_favourite else R.drawable.ic_favourite
        setImageBitmap(
            ContextCompat.getDrawable(context, id)?.mutate()?.toBitmap(dip(20), dip(20))
        )
    }

    fun updateShuffleButton(isShuffleOn: Int) = with(shuffle) {
        val id = when (isShuffleOn) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> R.drawable.ic_random
            else -> R.drawable.ic_random_off
        }
        setImageBitmap(
            AppCompatResources.getDrawable(context, id)?.mutate()
                ?.toBitmap(dip(20), dip(20))
        )
    }

    fun updateRepeatButton(repeatState: Int) = with(repeat) {
        val id = when (repeatState) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> R.drawable.ic_repeat_one
            PlaybackStateCompat.REPEAT_MODE_GROUP -> R.drawable.ic_repeat_queue
            PlaybackStateCompat.REPEAT_MODE_ALL -> R.drawable.ic_repeat_all
            else -> R.drawable.ic_repeat_off
        }
        setImageBitmap(
            AppCompatResources.getDrawable(context, id)?.mutate()
                ?.toBitmap(dip(20), dip(20))
        )
    }

    override fun getDimens(uiContext: UiContext<Context>): Dimens = Dimens(
        width = wrapContent,
        height = wrapContent
    )

    class Dimens(override var width: Int, override var height: Int) : BaseUiComponent.Dimens()

}