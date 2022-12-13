package com.shz.imagepicker.imagepicker.core

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.shz.imagepicker.imagepicker.ImagePicker
import com.shz.imagepicker.imagepicker.activity.customgallery.GalleryPickerCustomActivity
import com.shz.imagepicker.imagepicker.utils.RotationResult
import com.shz.imagepicker.imagepicker.utils.createTemporaryFile
import com.shz.imagepicker.imagepicker.utils.rotateImageIfRequired
import java.io.File

internal abstract class ImagePickerActivity : AppCompatActivity() {

    abstract val requestCode: Int

    private var autoRotate: Boolean = false

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

    protected fun File.applyConditionalRotation(): File {
        if (!autoRotate) return this
        return when (val rotationResult = rotateImageIfRequired(this)) {
            RotationResult.NotRequired -> this
            is RotationResult.Rotate -> {
                try {
                    createTemporaryFile(rotationResult.rotatedBitmap)
                } catch (e: Exception) {
                    ImagePicker.errorLog("Unable to create temporary rotated file", e)
                    this
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoRotate = intent.getBooleanExtra(BUNDLE_AUTO_ROTATE, false)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
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

    companion object {
        internal const val BUNDLE_AUTO_ROTATE = "bundle_auto_rotate"
    }
}
