package com.saeedlotfi.limlam.userInterface.layouts._commom.customViews

import android.content.Context
import android.graphics.*
import androidx.appcompat.widget.AppCompatImageView
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.BaseView

class RoundImageView(override var ctx: Context) : AppCompatImageView(ctx),
    BaseView<Context, AppCompatImageView> {

    private var shader: BitmapShader? = null
    private var image: Bitmap? = null
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var canvasSize = 0
    private var round = 10f
    private var mode = ImageViewMode.Normal

    fun setRound(round: Float) {
        this.round = round
    }

    fun setMode(mode: ImageViewMode) {
        this.mode = mode
    }

    override fun setImageBitmap(bm: Bitmap?) {
        alpha = 0f
        image = bm
        if (canvasSize > 0) updateBitmapShader()
        super.setImageBitmap(bm)
        animate()?.alpha(1f)?.duration = 500
    }

    private fun updateBitmapShader() {
        if (image == null) {
            return
        }
        if (image?.isRecycled == true) {
            return
        }
        image?.let { img ->
            val size = if (img.width > img.height) img.width else img.height
            val defHeight = size - img.height
            val defWidth = size - img.width
            Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).also { bmp ->
                val canvas = Canvas(bmp)
                Paint().also { p ->
                    p.color = Color.LTGRAY
                    canvas.drawRect(Rect(0, 0, size, size), p)
                    canvas.drawBitmap(img, Matrix().also {
                        it.setTranslate(defWidth / 2f, defHeight / 2f)
                    }, null)
                }
                image = bmp
            }
        }
        shader = BitmapShader(image!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (canvasSize != image!!.width || canvasSize != image!!.height) {
            val matrix = Matrix()
            val scale = canvasSize.toFloat() / image!!.width.toFloat()
            matrix.setScale(scale, scale)
            shader!!.setLocalMatrix(matrix)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(measureSpec: Int): Int {
        val result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = when (specMode) {
            MeasureSpec.EXACTLY -> { // The parent has determined an exact size for the child.
                specSize
            }
            MeasureSpec.AT_MOST -> { // The child can be as large as it wants up to the specified size.
                specSize
            }
            else -> { // The parent has not imposed any constraint on the child.
                canvasSize
            }
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        val result: Int
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        result = when (specMode) {
            MeasureSpec.EXACTLY -> { // We were told how big to be
                specSize
            }
            MeasureSpec.AT_MOST -> { // The child can be as large as it wants up to the specified size.
                specSize
            }
            else -> { // Measure the text (beware: ascent is a negative number)
                canvasSize
            }
        }
        return result + 2
    }

    public override fun onDraw(canvas: Canvas) {
        if (image == null) return
        if (image!!.height == 0 || image!!.width == 0) return

        val oldCanvasSize = canvasSize
        canvasSize = if (width < height) width else height
        if (oldCanvasSize != canvasSize) updateBitmapShader()

        paint.shader = shader
        paint.isAntiAlias = true
        paint.colorFilter = null

        when (mode) {
            ImageViewMode.Normal -> {
                canvas.drawRect(0f, 0f, canvasSize * 1f, canvasSize * 1f, paint)
            }
            ImageViewMode.Circle -> {
                canvas.drawCircle(canvasSize / 2f, canvasSize / 2f, canvasSize / 2f, paint)
            }
            ImageViewMode.Round -> {
                canvas.drawRoundRect(0f, 0f, canvasSize * 1f, canvasSize * 1f, round, round, paint)
            }
        }
    }

    enum class ImageViewMode {
        Normal, Round, Circle
    }

}