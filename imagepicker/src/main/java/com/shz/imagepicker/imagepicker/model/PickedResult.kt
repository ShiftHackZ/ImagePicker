package com.shz.imagepicker.imagepicker.model

import java.io.Serializable
import com.shz.imagepicker.imagepicker.ImagePickerCallback

/**
 * Describes all available business logic pick-operation result.
 *
 * Implements [Serializable].
 *
 * @see ImagePickerCallback
 */
sealed class PickedResult : Serializable {
    /**
     * Member of [PickedResult].
     * Determines that nothing was selected during pick-operation.
     */
    object Empty : PickedResult()

    /**
     * Member of [PickedResult].
     * Determines that only one image was selected during pick-operation.
     *
     * @param image instance of selected [PickedImage].
     *
     * @see PickedImage
     */
    data class Single(val image: PickedImage) : PickedResult()

    /**
     * Member of [PickedResult].
     * Determines that only multiple images were selected during pick-operation.
     *
     * @param images collection of selected [PickedImage].
     *
     * @see PickedImage
     */
    data class Multiple(val images: List<PickedImage>) : PickedResult()

    /**
     * Member of [PickedResult].
     * Determines that some error occurred during pick-operation.
     *
     * @param throwable issue that happened with operation.
     *
     * @see Throwable
     */
    data class Error(val throwable: Throwable) : PickedResult()
}
