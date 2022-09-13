package com.shz.imagepicker.imagepicker

import com.shz.imagepicker.imagepicker.model.PickedResult

/**
 * Contract interface, that allows to receive image-pick result as [PickedResult].
 *
 * @see PickedResult
 */
fun interface ImagePickerCallback {

    /**
     * Returns result of image-pick operation.
     *
     * @see PickedResult
     *
     * @param result actual result of last image-pick operation.
     *
     * Possible values:
     * - [PickedResult.Empty] if nothing was selected;
     * - [PickedResult.Single] in case one image was selected;
     * - [PickedResult.Multiple] in case multiple images were selected;
     * - [PickedResult.Error] in case some error occurred.
     */
    fun onImagePickerResult(result: PickedResult)
}
