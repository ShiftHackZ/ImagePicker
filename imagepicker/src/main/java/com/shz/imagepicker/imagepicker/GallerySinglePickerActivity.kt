package com.shz.imagepicker.imagepicker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.shz.imagepicker.imagepicker.ImagePath.getImagePathFromInputStreamUri
import java.io.File

class GallerySinglePickerActivity : AppCompatActivity() {

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data
                ?.data
                ?.let { uri -> getImagePathFromInputStreamUri(this, uri) }
                ?.let(::File)
                ?.takeIf(File::exists)
                ?.let { file -> PickerImage(PickerSource.GALLERY_SINGLE, file) }
                ?.let(PickerResult::Single)
                ?.let(callback::onImagePickerResult)
        }
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CHECK_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryPicker()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGalleryPermission(::startGalleryPicker)
    }

    private fun startGalleryPicker() {
        galleryLauncher.launch(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    GALLERY_IMAGE_MIME
                )
            }
        )
    }

    companion object {
        private const val GALLERY_IMAGE_MIME = "image/jpeg"

        @JvmField
        internal var callback: ImagePickerCallback = ImagePickerCallback {  }
    }
}