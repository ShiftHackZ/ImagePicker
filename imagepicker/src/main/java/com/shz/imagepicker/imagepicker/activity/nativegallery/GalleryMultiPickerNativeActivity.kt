package com.shz.imagepicker.imagepicker.activity.nativegallery

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.core.ImagePickerActivity
import com.shz.imagepicker.imagepicker.model.PickedImage
import com.shz.imagepicker.imagepicker.model.PickedResult
import com.shz.imagepicker.imagepicker.model.PickedSource
import com.shz.imagepicker.imagepicker.utils.checkGalleryNativePermission
import com.shz.imagepicker.imagepicker.utils.getImagePathFromInputStreamUri
import com.shz.imagepicker.imagepicker.utils.getNormalizedUri
import java.io.File

internal class GalleryMultiPickerNativeActivity : ImagePickerActivity() {

    override val requestCode: Int = 54501

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGalleryNativePermission(requestCode, ::startPicker)
    }

    override fun deliverResult(intent: Intent?) {
        super.deliverResult(intent)
        val output = arrayListOf<PickedImage>()
        intent?.clipData?.let { clipData ->
            for (i in 0 until clipData.itemCount) {
                getImagePathFromInputStreamUri(this, clipData.getItemAt(i).uri)
                    ?.let(::File)
                    ?.takeIf(File::exists)
                    ?.applyConditionalRotation()
                    ?.let { file -> PickedImage(PickedSource.GALLERY_MULTIPLE, file) }
                    ?.let(output::add)
            }
        }
        val samsungPickerList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.extras?.getParcelableArrayList(SAMSUNG_SELECTED_ITEMS, Parcelable::class.java)
        } else {
            intent?.extras?.getParcelableArrayList(SAMSUNG_SELECTED_ITEMS)
        }
        samsungPickerList?.forEach {
            (it as? Uri)?.let { uri ->
                getNormalizedUri(this, uri)?.path
                    ?.let(::File)
                    ?.takeIf(File::exists)
                    ?.applyConditionalRotation()
                    ?.let { file -> PickedImage(PickedSource.GALLERY_MULTIPLE, file) }
                    ?.let(output::add)
            }
        }
        when (output.size) {
            0 -> PickedResult.Empty
            1 -> PickedResult.Single(output.first())
            else -> PickedResult.Multiple(output)
        }.let(callback::onImagePickerResult)
    }

    override fun startPicker() {
        launcher.launch(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                putExtra(SAMSUNG_MULTI_PICK, true);
                setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    GALLERY_IMAGE_MIME,
                )
            }
        )
    }

    companion object {
        private const val GALLERY_IMAGE_MIME = "image/jpeg"
        private const val SAMSUNG_MULTI_PICK = "multi-pick"
        private const val SAMSUNG_SELECTED_ITEMS = "selectedItems"

        @JvmField
        internal var callback: ImagePickerCallback = ImagePickerCallback {  }
    }
}
