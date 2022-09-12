package com.shz.imagepicker.imagepicker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.File

data class PickerImage(
    val source: PickerSource,
    val file: File,
) {
    val bitmap: Bitmap
        get() = BitmapFactory.decodeFile(file.path)

    val bitmapDrawable: Drawable?
        get() = BitmapDrawable.createFromPath(file.path)
}
