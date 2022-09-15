package com.shz.imagepicker.imagepicker

import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.File

fun interface ImagePickerLoadDelegate {
    fun load(imageView: ImageView, file: File)
}

internal val defaultImagePickerLoadDelegate = ImagePickerLoadDelegate { iv, file ->
    iv.setImageBitmap(BitmapFactory.decodeFile(file.path))
}
