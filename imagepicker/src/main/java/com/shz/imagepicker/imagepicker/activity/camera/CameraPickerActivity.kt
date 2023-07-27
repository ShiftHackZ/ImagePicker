package com.shz.imagepicker.imagepicker.activity.camera

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.core.ImagePickerActivity
import com.shz.imagepicker.imagepicker.model.PickedImage
import com.shz.imagepicker.imagepicker.model.PickedResult
import com.shz.imagepicker.imagepicker.model.PickedSource
import com.shz.imagepicker.imagepicker.utils.checkCameraPermission
import com.shz.imagepicker.imagepicker.utils.getCaptureImageOutputUri
import com.shz.imagepicker.imagepicker.utils.getCaptureImageResultUri
import com.shz.imagepicker.imagepicker.utils.getNormalizedUri
import java.io.File

internal class CameraPickerActivity : ImagePickerActivity() {

    override val requestCode: Int = 54500

    private var filename: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkCameraPermission(requestCode, ::startPicker)
    }

    override fun deliverResult(intent: Intent?) {
        getCaptureImageResultUri(this, intent, filename)
            ?.let { uri -> getNormalizedUri(this, uri) }
            ?.path
            ?.let(::File)
            ?.let { file -> PickedImage(PickedSource.CAMERA, file) }
            ?.let(PickedResult::Single)
            ?.let(callback::onImagePickerResult)
    }

    override fun startPicker() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        filename = System.nanoTime().toString()
        val uri = getCaptureImageOutputUri(this, filename)
        if (filename.isNotEmpty() && authority.isNotEmpty()) uri?.path?.let { path ->
            val file = File(path)
            if (Build.VERSION.SDK_INT >= 24) {
                cameraIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
                        this,
                        authority,
                        file,
                    )
                )
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            launcher.launch(cameraIntent)
        }
    }

    companion object {
        @JvmField
        internal var callback: ImagePickerCallback = ImagePickerCallback { }
        internal var authority: String = ""
    }
}
