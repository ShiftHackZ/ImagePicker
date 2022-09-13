package com.shz.imagepicker.imagepicker.model

import java.io.Serializable

sealed class PickedResult : Serializable {
    object Empty : PickedResult()
    data class Single(val image: PickedImage) : PickedResult()
    data class Multiple(val images: List<PickedImage>) : PickedResult()
    data class Error(val throwable: Throwable) : PickedResult()
}
