package com.shz.imagepicker.imagepicker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.shz.imagepicker.imagepicker.ImagePath.getImagePathFromInputStreamUri
import java.io.File

class GalleryMultiPickerActivity : AppCompatActivity() {

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val output = arrayListOf<PickerImage>()
            result.data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    getImagePathFromInputStreamUri(this, clipData.getItemAt(i).uri)
                        ?.let(::File)
                        ?.takeIf(File::exists)
                        ?.let { file -> PickerImage(PickerSource.GALLERY_MULTIPLE, file) }
                        ?.let(output::add)
                }
            }
            when (output.size) {
                0 -> null
                1 -> PickerResult.Single(output.first())
                else -> PickerResult.Multiple(output)
            }?.let(callback::onImagePickerResult)
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
                if (Build.VERSION.SDK_INT >= 18) putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
