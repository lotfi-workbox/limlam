package com.saeedlotfi.limlam.userInterface._common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream

object BitmapDiskCache {

    @OptIn(DelicateCoroutinesApi::class)
    fun getImage(id: Int, onComplete: (bitmap: Bitmap?) -> Unit) {
        GlobalScope.launch {
            val opt = BitmapFactory.Options()
            opt.inSampleSize = 2
            opt.inPreferredConfig = Bitmap.Config.RGB_565
            BitmapFactory.decodeFile("${Constants.cachePath}$id.jpeg", opt).also { bitmap ->
                MainScope().launch {
                    onComplete.invoke(bitmap)
                }
            }
        }
    }

    fun putImage(id: Int, image: Bitmap) {
        File(Constants.cachePath, "$id.jpeg").also { file ->
            if (!file.exists()) {
                file.createNewFile()
                val out = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.JPEG, 25, out)
                out.flush()
                out.close()
            }
        }
    }

}