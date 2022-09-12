package com.shz.imagepicker.imagepicker

sealed class PickerResult {
    data class Single(val image: PickerImage) : PickerResult()
    data class Multiple(val images: List<PickerImage>) : PickerResult()
}
