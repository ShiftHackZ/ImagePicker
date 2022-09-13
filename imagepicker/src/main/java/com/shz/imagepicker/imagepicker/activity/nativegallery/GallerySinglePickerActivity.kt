package com.shz.imagepicker.imagepicker.activity.nativegallery

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.core.ImagePickerActivity
import com.shz.imagepicker.imagepicker.model.PickedImage
import com.shz.imagepicker.imagepicker.model.PickedResult
import com.shz.imagepicker.imagepicker.model.PickedSource
import com.shz.imagepicker.imagepicker.utils.checkGalleryNativePermission
import com.shz.imagepicker.imagepicker.utils.getImagePathFromInputStreamUri
import java.io.File

internal class GallerySinglePickerActivity : ImagePickerActivity() {

    override val requestCode: Int = 54502

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGalleryNativePermission(requestCode, ::startPicker)
    }

    override fun deliverResult(intent: Intent?) {
        super.deliverResult(intent)
        intent
            ?.data
            ?.let { uri -> getImagePathFromInputStreamUri(this, uri) }
            ?.let(::File)
            ?.takeIf(File::exists)
            ?.let { file -> PickedImage(PickedSource.GALLERY_SINGLE, file) }
            ?.let(PickedResult::Single)
            ?.let(callback::onImagePickerResult)
    }

    override fun startPicker() {
        launcher.launch(
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