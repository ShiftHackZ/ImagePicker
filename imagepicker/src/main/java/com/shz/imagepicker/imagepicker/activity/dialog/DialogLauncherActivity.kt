package com.shz.imagepicker.imagepicker.activity.dialog

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shz.imagepicker.imagepicker.ImagePicker
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.ImagePickerLauncher
import com.shz.imagepicker.imagepicker.exception.UnableLaunchDialogException
import com.shz.imagepicker.imagepicker.model.GallerySelector
import java.io.Serializable

internal class DialogLauncherActivity : AppCompatActivity(), ImagePickerDialog.Listener {

    private var payload: Payload? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.payload = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra(BUNDLE_PAYLOAD, Payload::class.java)
        } else {
            intent.getSerializableExtra(BUNDLE_PAYLOAD) as Payload?
        }

        this.payload?.let {
            ImagePickerDialog(this).show(supportFragmentManager, PICKER_DIALOG_TAG)
        } ?: run {
            ImagePicker.deliverThrowable(callback, UnableLaunchDialogException())
            finish()
        }
    }

    override fun launchCamera() {
        ImagePickerLauncher(this).launchCameraPicker(
            authority = payload?.authority ?: "",
            callback = callback,
        )
        finish()
    }

    override fun launchGallery() {
        ImagePickerLauncher(this).launchGalleryPicker(
            callback = callback,
            multipleSelection = payload?.multipleSelection ?: false,
            gallerySelector = payload?.gallerySelector ?: GallerySelector.NATIVE,
            minimum = payload?.minimum ?: 1,
            maximum = payload?.maximum ?: 10,
        )
        finish()
    }

    override fun onDismiss() {
        finish()
    }

    data class Payload(
        val authority: String,
        val multipleSelection: Boolean,
        val minimum: Int,
        val maximum: Int,
        val gallerySelector: GallerySelector,
    ) : Serializable

    companion object {
        private const val PICKER_DIALOG_TAG = "picker_dialog_tag"

        internal const val BUNDLE_PAYLOAD = "bundle_payload"

        @JvmField
        internal var callback: ImagePickerCallback = ImagePickerCallback {  }
    }
}
