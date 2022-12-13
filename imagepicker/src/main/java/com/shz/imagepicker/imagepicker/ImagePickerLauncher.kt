package com.shz.imagepicker.imagepicker

import android.content.Context
import android.content.Intent
import com.shz.imagepicker.imagepicker.activity.camera.CameraPickerActivity
import com.shz.imagepicker.imagepicker.activity.customgallery.GalleryPickerCustomActivity
import com.shz.imagepicker.imagepicker.activity.dialog.DialogLauncherActivity
import com.shz.imagepicker.imagepicker.activity.nativegallery.GalleryMultiPickerNativeActivity
import com.shz.imagepicker.imagepicker.activity.nativegallery.GallerySinglePickerActivity
import com.shz.imagepicker.imagepicker.core.ImagePickerActivity
import com.shz.imagepicker.imagepicker.model.GalleryPicker

internal class ImagePickerLauncher(private val context: Context) {

    fun launchCameraPicker(
        authority: String,
        callback: ImagePickerCallback,
    ) {
        ImagePicker.debugLog("[Launcher] launchCameraPicker")
        CameraPickerActivity.authority = authority
        CameraPickerActivity.callback = callback
        context.startActivity(Intent(context, CameraPickerActivity::class.java))
    }

    fun launchGalleryPicker(
        callback: ImagePickerCallback,
        delegate: ImagePickerLoadDelegate,
        multipleSelection: Boolean,
        autoRotate: Boolean,
        minimum: Int,
        maximum: Int,
        galleryPicker: GalleryPicker,
    ) {
        ImagePicker.debugLog("[Launcher] launchGalleryPicker")
        when (galleryPicker) {
            GalleryPicker.NATIVE -> if (multipleSelection) {
                launchMultipleSelectionGalleryNative(callback, autoRotate)
            } else {
                launchSingleSelectionGallery(callback, autoRotate)
            }
            GalleryPicker.CUSTOM -> {
                launchMultipleSelectionGalleryCustom(
                    callback = callback,
                    delegate = delegate,
                    multipleSelection = multipleSelection,
                    autoRotate = autoRotate,
                    min = minimum,
                    max = maximum
                )
            }
        }
    }

    fun launchDialog(
        authority: String,
        callback: ImagePickerCallback,
        delegate: ImagePickerLoadDelegate,
        multipleSelection: Boolean,
        autoRotate: Boolean,
        minimum: Int,
        maximum: Int,
        galleryPicker: GalleryPicker,
    ) {
        ImagePicker.debugLog("[Launcher] launchDialog")
        DialogLauncherActivity.callback = callback
        DialogLauncherActivity.loadDelegate = delegate
        context.startActivity(Intent(context, DialogLauncherActivity::class.java).apply {
            putExtra(
                DialogLauncherActivity.BUNDLE_PAYLOAD,
                DialogLauncherActivity.Payload(
                    authority,
                    multipleSelection,
                    autoRotate,
                    minimum,
                    maximum,
                    galleryPicker,
                )
            )
        })
    }

    private fun launchSingleSelectionGallery(
        callback: ImagePickerCallback,
        autoRotate: Boolean,
    ) {
        ImagePicker.debugLog("[Launcher] launchSingleSelectionGallery")
        GallerySinglePickerActivity.callback = callback
        context.startActivity(Intent(context, GallerySinglePickerActivity::class.java).apply {
            putExtra(ImagePickerActivity.BUNDLE_AUTO_ROTATE, autoRotate)
        })
    }

    private fun launchMultipleSelectionGalleryNative(
        callback: ImagePickerCallback,
        autoRotate: Boolean,
    ) {
        ImagePicker.debugLog("[Launcher] launchMultipleSelectionGalleryNative")
        GalleryMultiPickerNativeActivity.callback = callback
        context.startActivity(Intent(context, GalleryMultiPickerNativeActivity::class.java).apply {
            putExtra(ImagePickerActivity.BUNDLE_AUTO_ROTATE, autoRotate)
        })
    }

    @Deprecated("GalleryPicker.CUSTOM is deprecated from Android 13 (SDK 33)")
    private fun launchMultipleSelectionGalleryCustom(
        callback: ImagePickerCallback,
        delegate: ImagePickerLoadDelegate,
        multipleSelection: Boolean,
        autoRotate: Boolean,
        min: Int,
        max: Int,
    ) {
        ImagePicker.debugLog("[Launcher] launchMultipleSelectionGalleryCustom")
        GalleryPickerCustomActivity.loadDelegate = delegate
        GalleryPickerCustomActivity.callback = callback
        context.startActivity(Intent(context, GalleryPickerCustomActivity::class.java).apply {
            putExtra(ImagePickerActivity.BUNDLE_AUTO_ROTATE, autoRotate)
            putExtra(GalleryPickerCustomActivity.BUNDLE_MULTI_SELECTION, multipleSelection)
            putExtra(GalleryPickerCustomActivity.BUNDLE_MINIMUM, min)
            putExtra(GalleryPickerCustomActivity.BUNDLE_MAXIMUM, max)
        })
    }
}
