@file:Suppress("unused")

package com.saeedlotfi.limlam.userInterface._common

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.File

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Int.createTimeLabel(): String {
    var timeLabel: String
    if (this < 0) {
        timeLabel = "00:00"
    } else {
        val min = this / 1000 / 60
        val sec = this / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec
    }
    return timeLabel
}


fun File.getType(): String? {
    var type: String? = null
    val lastDot = path.lastIndexOf('.')
    if (lastDot != -1) {
        type = path.substring(lastDot, path.length)
    }
    return type
}

fun Context.shareMusics(musics: List<com.saeedlotfi.limlam.domain.model.MusicDoModel>) {
    val uris: ArrayList<Uri> = arrayListOf()
    musics.map {
        uris.add(
            FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                File(it.path)
            )
        )
    }
    Intent(Intent.ACTION_SEND_MULTIPLE).also {
        it.type = "audio/mp3"
        it.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        startActivity(Intent.createChooser(it, "share..."))
    }
}

fun Context.getStoragePaths(): MutableList<String> {
    val f = ContextCompat.getExternalFilesDirs(this, null)
    val paths = mutableListOf<String>()
    f.map { file ->
        file?.parent?.replace("/Android/data/", "")?.replace(this.packageName, "")
            ?.also { path ->
                paths.remove(path)
                paths.add(path)
            }
    }
    Log.i("storagePathsLog", "$paths")
    return paths
}

fun Context.sendBroadCast(action: String, key: String? = null) {
    val intent = Intent(action)
    intent.putExtra(action, key)
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
}

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val am = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
    @Suppress("DEPRECATION")
    val services = am.getRunningServices(Int.MAX_VALUE)
    return services.any { it.service.className == serviceClass.name }
}

