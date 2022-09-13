package com.shz.imagepicker.imagepicker

import com.shz.imagepicker.imagepicker.model.PickedResult

fun interface ImagePickerCallback {
    fun onImagePickerResult(result: PickedResult)
}
