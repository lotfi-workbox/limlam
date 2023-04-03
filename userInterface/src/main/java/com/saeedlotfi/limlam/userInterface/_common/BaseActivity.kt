package com.saeedlotfi.limlam.userInterface._common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

open class BaseActivity : AppCompatActivity() {

    private var onStoragePermissionRequestGranted: (() -> Unit)? = null

    private var doubleBackToExitPressedOnce = false

    fun getStoragePermission(onRequestGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val resultLauncher = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        onRequestGranted()
                    }
                }

                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(String.format("package:%s", applicationContext.packageName))
                    resultLauncher.launch(intent)
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    resultLauncher.launch(intent)
                }
            } else {
                onRequestGranted()
            }
        } else {
            when (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) {
                PackageManager.PERMISSION_GRANTED -> {
                    onRequestGranted()
                }
                PackageManager.PERMISSION_DENIED -> {
                    onStoragePermissionRequestGranted = onRequestGranted
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE.hashCode()
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE.hashCode() -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStoragePermissionRequestGranted?.invoke()
                    onStoragePermissionRequestGranted = null
                }
            }
        }
    }

    fun doubleTabToExit(onExit: (() -> Unit)? = null) {
        if (doubleBackToExitPressedOnce) {
            onExit?.invoke()
            finish()
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        window.decorView.handler.postDelayed(
            { doubleBackToExitPressedOnce = false },
            2000
        )
    }

}