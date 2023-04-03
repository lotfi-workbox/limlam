package com.saeedlotfi.limlam.userInterface._common

import android.graphics.Bitmap
import android.util.LruCache

object BitmapMemoryCache : LruCache<String, Bitmap>(
    (Runtime.getRuntime().maxMemory() / 1024).toInt() / 10
) {

    override fun sizeOf(key: String?, value: Bitmap?): Int {
        if (value != null) return value.byteCount / 1024
        return 0
    }

    fun getImage(key: String): Bitmap? = get(key)

    fun putImage(key: String, bitmap: Bitmap): Bitmap? = put(key, bitmap)

    fun removeImage(key: String) = remove(key)?.recycle()

}