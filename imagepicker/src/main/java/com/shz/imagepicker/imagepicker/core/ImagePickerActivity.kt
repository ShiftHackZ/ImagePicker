package com.shz.imagepicker.imagepicker.core

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.shz.imagepicker.imagepicker.ImagePicker

internal abstract class ImagePickerActivity : AppCompatActivity() {

    abstract val requestCode: Int

    protected val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            deliverResult(result.data)
        }
        finish()
    }

    open fun deliverResult(intent: Intent?) {
        ImagePicker.debugLog("deliverResult -> URI: ${intent?.data}")
    }

    open fun handleMissingPermissions() {
        finish()
    }

    abstract fun startPicker()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && !grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
                startPicker()
            } else {
                handleMissingPermissions()
            }
        }
    }
}
