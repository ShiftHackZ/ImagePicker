@file:Suppress("unused")

package com.shz.imagepicker.imagepicker.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import java.io.File
import java.io.Serializable

/**
 * Data model of picked image.
 *
 * Implements [Serializable].
 *
 * @see ImagePickerCallback
 */
data class PickedImage(
    /**
     * Describes the source of pick-operation of current image.
     *
     * @see PickedSource
     */
    val source: PickedSource,
    /**
     * Contains instance of [File] that corresponds to picked image.
     *
     * @see File
     */
    val file: File,
) : Serializable {

    /**
     * Field that represents picked image as [Bitmap] instance.
     *
     * @see Bitmap
     */
    val bitmap: Bitmap
        get() = BitmapFactory.decodeFile(file.path)

    /**
     * Field that represents picked image as [Drawable] instance.
     * This field is nullable.
     *
     * @see Drawable
     */
    val bitmapDrawable: Drawable?
        get() = BitmapDrawable.createFromPath(file.path)

}
