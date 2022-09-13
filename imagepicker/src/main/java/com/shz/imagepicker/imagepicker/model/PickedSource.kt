package com.shz.imagepicker.imagepicker.model

import java.io.Serializable
import com.shz.imagepicker.imagepicker.ImagePickerCallback

/**
 * Describes the source where particular [PickedImage] was selected from.
 * Useful to implement your local business logic, when handling result from [ImagePickerCallback].
 *
 * @see PickedImage
 * @see ImagePickerCallback
 */
enum class PickedSource : Serializable {
    /**
     * Means that image was selected from **single selection** [GalleryPicker.NATIVE].
     */
    GALLERY_SINGLE,

    /**
     * Means that image was selected from **multiple selection** [GalleryPicker.NATIVE].
     */
    GALLERY_MULTIPLE,

    /**
     * Means that image was selected from [GalleryPicker.CUSTOM].
     */
    GALLERY_CUSTOM,

    /**
     * Means that image was selected from **Camera Picker**.
     */
    CAMERA;
}
