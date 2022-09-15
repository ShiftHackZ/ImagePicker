package com.shz.imagepicker.imagepicker

import android.content.Context
import android.content.Intent
import com.shz.imagepicker.imagepicker.activity.camera.CameraPickerActivity
import com.shz.imagepicker.imagepicker.activity.customgallery.GalleryPickerCustomActivity
import com.shz.imagepicker.imagepicker.activity.dialog.DialogLauncherActivity
import com.shz.imagepicker.imagepicker.activity.nativegallery.GalleryMultiPickerNativeActivity
import com.shz.imagepicker.imagepicker.activity.nativegallery.GallerySinglePickerActivity
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
        minimum: Int,
        maximum: Int,
        galleryPicker: GalleryPicker,
    ) {
        ImagePicker.debugLog("[Launcher] launchGalleryPicker")
        when (galleryPicker) {
            GalleryPicker.NATIVE -> if (multipleSelection) {
                launchMultipleSelectionGalleryNative(callback)
            } else {
                launchSingleSelectionGallery(callback)
            }
            GalleryPicker.CUSTOM -> {
                launchMultipleSelectionGalleryCustom(
                    callback,
                    delegate,
                    multipleSelection,
                    minimum,
                    maximum
                )
            }
        }
    }

    fun launchDialog(
        authority: String,
        callback: ImagePickerCallback,
        delegate: ImagePickerLoadDelegate,
        multipleSelection: Boolean,
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
                    minimum,
                    maximum,
                    galleryPicker,
                )
            )
        })
    }

    private fun launchSingleSelectionGallery(callback: ImagePickerCallback) {
        ImagePicker.debugLog("[Launcher] launchSingleSelectionGallery")
        GallerySinglePickerActivity.callback = callback
        context.startActivity(Intent(context, GallerySinglePickerActivity::class.java))
    }

    private fun launchMultipleSelectionGalleryNative(callback: ImagePickerCallback) {
        ImagePicker.debugLog("[Launcher] launchMultipleSelectionGalleryNative")
        GalleryMultiPickerNativeActivity.callback = callback
        context.startActivity(Intent(context, GalleryMultiPickerNativeActivity::class.java))
    }

    private fun launchMultipleSelectionGalleryCustom(
        callback: ImagePickerCallback,
        delegate: ImagePickerLoadDelegate,
        multipleSelection: Boolean,
        min: Int,
        max: Int,
    ) {
        ImagePicker.debugLog("[Launcher] launchMultipleSelectionGalleryCustom")
        GalleryPickerCustomActivity.loadDelegate = delegate
        GalleryPickerCustomActivity.callback = callback
        context.startActivity(Intent(context, GalleryPickerCustomActivity::class.java).apply {
            putExtra(GalleryPickerCustomActivity.BUNDLE_MULTI_SELECTION, multipleSelection)
            putExtra(GalleryPickerCustomActivity.BUNDLE_MINIMUM, min)
            putExtra(GalleryPickerCustomActivity.BUNDLE_MAXIMUM, max)
        })
    }
}
