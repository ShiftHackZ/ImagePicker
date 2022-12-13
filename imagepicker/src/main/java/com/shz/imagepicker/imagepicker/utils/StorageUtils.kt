package com.shz.imagepicker.imagepicker.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

private const val STORAGE_ROTATED = "rotated"

internal fun Context.getStorageDirectory(name: String): File {
    val rootDirectory: File = filesDir
    if (!rootDirectory.exists()) rootDirectory.mkdir()
    val directory = File(rootDirectory, name)
    if (!directory.exists()) directory.mkdir()
    return directory
}

internal fun Context.createTemporaryFile(bitmap: Bitmap): File {
    val directory = getStorageDirectory(STORAGE_ROTATED)
    val filename = "${System.currentTimeMillis()}.jpg"
    return File(directory, filename).apply {
        writeBitmap(bitmap, Bitmap.CompressFormat.JPEG, 100)
    }
}

internal fun File.writeBitmap(
    bitmap: Bitmap,
    format: Bitmap.CompressFormat,
    quality: Int = 100,
) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}
