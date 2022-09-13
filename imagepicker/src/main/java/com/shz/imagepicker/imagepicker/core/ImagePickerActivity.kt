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

    abstract fun startPicker()

    open fun deliverResult(intent: Intent?) = Unit

    open fun handleMissingPermissions() {
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startPicker()
            } else {
                handleMissingPermissions()
            }
        }
    }
}
