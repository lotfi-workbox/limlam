package com.saeedlotfi.limlam.userInterface.layouts.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.saeedlotfi.limlam.userInterface._common.BitmapDiskCache
import com.saeedlotfi.limlam.userInterface._common.BitmapMemoryCache
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.*
import com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.RoundImageView
import com.saeedlotfi.limlam.userInterface.layouts.components.ThumbnailUiComponent.*
import javax.inject.Inject

open class ThumbnailUiComponent @Inject constructor() :
    BaseUiComponent<Context, RoundImageView, Dimens>() {

    override lateinit var rootView: RoundImageView
    var id: Int? = null

    @SuppressLint("RtlHardcoded")
    override fun createView(uiContext: UiContext<Context>, dimens: Dimens) = with(uiContext) {
        roundImageView {
            id = View.generateViewId()
            setMode(RoundImageView.ImageViewMode.Round)
            setRound(dip(10).toFloat())
        }.lParams<_, vGroup_lp>(dimens.width, dimens.height)
    }

    fun loadThumbnailFromCache(id: Long, @DrawableRes default: Int) {
        val image = BitmapMemoryCache.getImage("$id-${rootView.hashCode()}")
        if (image == null) BitmapDiskCache.getImage(id.toInt()) { bmp ->
            if (bmp == null) {
                setImageResource(default)
                return@getImage
            }
            BitmapMemoryCache.putImage("$id-${rootView.hashCode()}", bmp)
            rootView.post { rootView.setImageBitmap(bmp) }
        } else rootView.setImageBitmap(image)
    }

    fun recycleImage() {
        rootView.setImageBitmap(null)
        BitmapMemoryCache.removeImage("$id-${rootView.hashCode()}")
    }

    fun setImageResource(@DrawableRes id: Int) = with(rootView) {
        post {
            try {
                setImageBitmap(
                    ContextCompat.getDrawable(context, id)?.mutate()
                        ?.toBitmap(width + dip(1), height + dip(1))
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