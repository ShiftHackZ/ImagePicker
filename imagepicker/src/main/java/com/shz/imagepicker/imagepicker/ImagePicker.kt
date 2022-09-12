@file:Suppress("unused")

package com.shz.imagepicker.imagepicker

import android.content.Context
import android.content.Intent

class ImagePicker private constructor(
    private val authority: String,
    private val callback: ImagePickerCallback = ImagePickerCallback { },
    private val useCamera: Boolean = true,
    private val useGallery: Boolean = false,
    private val multipleSelection: Boolean = false,
    private val multipleSelectionType: MultipleSelectionType = MultipleSelectionType.NATIVE,
) {

    fun launch(context: Context) {
        when {
            useCamera && !useGallery -> {
                launchCameraPicker(context)
            }
            useGallery && !multipleSelection && !useCamera -> {
                launchSingleSelectionGallery(context)
            }
            useGallery && multipleSelection && !useCamera -> when (multipleSelectionType) {
                MultipleSelectionType.NATIVE -> launchMultipleSelectionGalleryNative(context)
                MultipleSelectionType.CUSTOM -> launchMultipleSelectionGalleryCustom(context)
            }
            else -> Unit
        }
    }

    private fun launchCameraPicker(context: Context) {
        CameraPickerActivity.callback = callback
        CameraPickerActivity.authority = authority
        context.startActivity(Intent(context, CameraPickerActivity::class.java))
    }

    private fun launchSingleSelectionGallery(context: Context) {
        GallerySinglePickerActivity.callback = callback
        context.startActivity(Intent(context, GallerySinglePickerActivity::class.java))
    }

    private fun launchMultipleSelectionGalleryNative(context: Context) {
        GalleryMultiPickerActivity.callback = callback
        context.startActivity(Intent(context, GalleryMultiPickerActivity::class.java))
    }

    private fun launchMultipleSelectionGalleryCustom(context: Context) {
        //ToDo: implement me
    }

    class Builder(
        private val authority: String,
        private val callback: ImagePickerCallback,
    ) {
        private var useCamera: Boolean = false
        private var useGallery: Boolean = false
        private var multipleSelection: Boolean = false
        private var multipleSelectionType: MultipleSelectionType = MultipleSelectionType.NATIVE

        fun build(): ImagePicker = ImagePicker(
            authority,
            callback,
            useCamera,
            useGallery,
            multipleSelection,
        )

        fun useCamera(useCamera: Boolean = true) = apply {
            this.useCamera = useCamera
        }

        fun useGallery(useGallery: Boolean = true) = apply {
            this.useGallery = useGallery
        }

        fun multipleSelection(multipleSelection: Boolean) = apply {
            this.multipleSelection = multipleSelection
        }

        fun multipleSelectionType(multipleSelectionType: MultipleSelectionType) = apply {
            this.multipleSelectionType = multipleSelectionType
        }

        companion object {

            inline fun build(
                authority: String,
                callback: ImagePickerCallback,
                block: Builder.() -> Unit,
            ) = Builder(authority, callback)
                .apply(block)
                .build()
        }
    }
}
