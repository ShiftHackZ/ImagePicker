package com.shz.imagepicker.imagepicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.shz.imagepicker.imagepicker.ImagePath.getCaptureImageResultUri
import com.shz.imagepicker.imagepicker.ImagePath.getNormalizedUri
import com.shz.imagepicker.imagepicker.ImagePath.getCaptureImageOutputUri
import java.io.File

class CameraPickerActivity : AppCompatActivity() {

    private var filename: String = ""

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getCaptureImageResultUri(this, result.data, filename)
                ?.let { uri -> getNormalizedUri(this, uri) }
                ?.path
                ?.let(::File)
                ?.let { file -> PickerImage(PickerSource.CAMERA, file) }
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
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraPicker()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA
            )
        } else {
            startCameraPicker()
        }
    }

    private fun startCameraPicker() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        filename = System.nanoTime().toString()
        val uri = getCaptureImageOutputUri(this, filename)
        if (filename.isNotEmpty()) uri?.path?.let { path ->
            val file = File(path)
            if (Build.VERSION.SDK_INT >= 24) {
                cameraIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
                        this,
                        authority,
                        file
                    )
                )
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            cameraLauncher.launch(cameraIntent)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 54560

        @JvmField
        internal var callback: ImagePickerCallback = ImagePickerCallback { }
        internal var authority: String = ""
    }
}
