package com.shz.imagepicker.imagepicker

import android.content.Context
import android.content.Intent
import com.shz.imagepicker.imagepicker.activity.camera.CameraPickerActivity
import com.shz.imagepicker.imagepicker.activity.customgallery.GalleryPickerCustomActivity
import com.shz.imagepicker.imagepicker.activity.dialog.DialogLauncherActivity
import com.shz.imagepicker.imagepicker.activity.nativegallery.GalleryMultiPickerNativeActivity
import com.shz.imagepicker.imagepicker.activity.nativegallery.GallerySinglePickerActivity
import com.shz.imagepicker.imagepicker.model.GallerySelector
import kotlin.math.max

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
        multipleSelection: Boolean,
        minimum: Int,
        maximum: Int,
        gallerySelector: GallerySelector,
    ) {
        ImagePicker.debugLog("[Launcher] launchGalleryPicker")
        when (gallerySelector) {
            GallerySelector.NATIVE -> if (multipleSelection) {
                launchMultipleSelectionGalleryNative(callback)
            } else {
                launchSingleSelectionGallery(callback)
            }
            GallerySelector.CUSTOM -> {
                launchMultipleSelectionGalleryCustom(callback, multipleSelection, minimum, maximum)
            }
        }
    }

    fun launchDialog(
        authority: String,
        callback: ImagePickerCallback,
        multipleSelection: Boolean,
        minimum: Int,
        maximum: Int,
        gallerySelector: GallerySelector,
    ) {
        ImagePicker.debugLog("[Launcher] launchDialog")
        DialogLauncherActivity.callback = callback
        context.startActivity(Intent(context, DialogLauncherActivity::class.java).apply {
            putExtra(
                DialogLauncherActivity.BUNDLE_PAYLOAD,
                DialogLauncherActivity.Payload(
                    authority,
                    multipleSelection,
                    minimum,
                    maximum,
                    gallerySelector,
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
        multipleSelection: Boolean,
        min: Int,
        max: Int,
    ) {
        ImagePicker.debugLog("[Launcher] launchMultipleSelectionGalleryCustom")
        GalleryPickerCustomActivity.callback = callback
        context.startActivity(Intent(context, GalleryPickerCustomActivity::class.java).apply {
            putExtra(GalleryPickerCustomActivity.BUNDLE_MULTI_SELECTION, multipleSelection)
            putExtra(GalleryPickerCustomActivity.BUNDLE_MINIMUM, min)
            putExtra(GalleryPickerCustomActivity.BUNDLE_MAXIMUM, max)
        })
    }
}
