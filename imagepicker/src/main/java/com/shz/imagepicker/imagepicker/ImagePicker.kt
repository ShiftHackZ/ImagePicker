@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.shz.imagepicker.imagepicker

import android.content.Context
import android.util.Log
import com.shz.imagepicker.imagepicker.exception.NothingToLaunchException
import com.shz.imagepicker.imagepicker.model.GallerySelector
import com.shz.imagepicker.imagepicker.model.PickedResult

class ImagePicker private constructor(
    private val authority: String,
    private val callback: ImagePickerCallback,
    private val useCamera: Boolean,
    private val useGallery: Boolean,
    private val multipleSelection: Boolean,
    private val minimumSelectionCount: Int,
    private val maximumSelectionCount: Int,
    private val gallerySelector: GallerySelector,
) {

    fun launch(context: Context) = with(ImagePickerLauncher(context)) {
        when {
            useCamera && !useGallery -> launchCameraPicker(authority, callback)
            !useCamera && useGallery -> launchGalleryPicker(
                callback,
                multipleSelection,
                minimumSelectionCount,
                maximumSelectionCount,
                gallerySelector
            )
            useCamera && useGallery -> launchDialog(
                authority,
                callback,
                multipleSelection,
                minimumSelectionCount,
                maximumSelectionCount,
                gallerySelector
            )
            else -> deliverThrowable(callback, NothingToLaunchException())
        }
    }

    class Builder(
        private val authority: String,
        private val callback: ImagePickerCallback,
    ) {
        private var useCamera: Boolean = false
        private var useGallery: Boolean = false
        private var multipleSelection: Boolean = false
        private var minimumSelectionCount: Int = 1
        private var maximumSelectionCount: Int = Int.MAX_VALUE
        private var gallerySelector: GallerySelector = GallerySelector.NATIVE

        fun build(): ImagePicker {
            require(minimumSelectionCount >= 1) {
                "Parameter minimumSelectionCount must be bigger or equal 1"
            }
            require(maximumSelectionCount > minimumSelectionCount) {
                "Parameter maximumSelectionCount must be bigger than minimumSelectionCount"
            }
            return ImagePicker(
                authority,
                callback,
                useCamera,
                useGallery,
                multipleSelection,
                minimumSelectionCount,
                maximumSelectionCount,
                gallerySelector,
            )
        }

        fun useCamera(useCamera: Boolean = true) = apply {
            this.useCamera = useCamera
        }

        fun useGallery(useGallery: Boolean = true) = apply {
            this.useGallery = useGallery
        }

        fun multipleSelection(multipleSelection: Boolean = true) = apply {
            this.multipleSelection = multipleSelection
        }

        fun minimumSelectionCount(minimumSelectionCount: Int) = apply {
            this.minimumSelectionCount = minimumSelectionCount
        }

        fun maximumSelectionCount(maximumSelectionCount: Int) = apply {
            this.maximumSelectionCount = maximumSelectionCount
        }

        fun gallerySelector(gallerySelector: GallerySelector) = apply {
            this.gallerySelector = gallerySelector
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

    companion object {
        private const val TAG = "ShzImagePicker"

        internal inline fun deliverThrowable(clb: ImagePickerCallback, t: Throwable) {
            t.also(::errorLog)
                .let(PickedResult::Error)
                .let(clb::onImagePickerResult)
        }

        internal inline fun errorLog(t: Throwable) {
            if (BuildConfig.DEBUG) Log.e(TAG, t.message.toString(), t)
        }

        internal inline fun errorLog(message: String, t: Throwable? = null) {
            if (BuildConfig.DEBUG) Log.e(TAG, message, t)
        }

        internal inline fun debugLog(message: String) {
            if (BuildConfig.DEBUG) Log.d(TAG, message)
        }
    }
}
