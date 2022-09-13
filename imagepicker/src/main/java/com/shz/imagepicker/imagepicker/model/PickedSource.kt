package com.shz.imagepicker.imagepicker.model

import java.io.Serializable

enum class PickedSource : Serializable {
    GALLERY_SINGLE,
    GALLERY_MULTIPLE,
    GALLERY_CUSTOM,
    CAMERA;
}
