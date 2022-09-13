@file:Suppress("unused")

package com.shz.imagepicker.imagepicker.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.File
import java.io.Serializable

data class PickedImage(
    val source: PickedSource,
    val file: File,
) : Serializable {

    val bitmap: Bitmap
        get() = BitmapFactory.decodeFile(file.path)

    val bitmapDrawable: Drawable?
        get() = BitmapDrawable.createFromPath(file.path)

}
