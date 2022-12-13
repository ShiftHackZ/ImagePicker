package com.shz.imagepicker.imagepicker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File

internal sealed class RotationResult {
    object NotRequired : RotationResult()
    data class Rotate(
        val angle: Float,
        val rotatedBitmap: Bitmap,
    ) : RotationResult()
}

internal fun rotateImageIfRequired(inputFile: File): RotationResult {
    val inputBitmap = BitmapFactory.decodeFile(inputFile.absolutePath)
    val exifInterface = ExifInterface(inputFile.absolutePath)
    val orientation: Int = exifInterface.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED,
    )
    val angle: Float = when (orientation) {
        ExifInterface.ORIENTATION_NORMAL -> 0f
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }
    return when (angle) {
        0f -> RotationResult.NotRequired
        else -> RotationResult.Rotate(
            angle,
            inputBitmap.rotate(angle)
        )
    }
}

internal fun Bitmap.rotate(angle: Float): Bitmap = Bitmap.createBitmap(
    this,
    0,
    0,
    width,
    height,
    Matrix().apply { postRotate(angle) },
    true,
)
