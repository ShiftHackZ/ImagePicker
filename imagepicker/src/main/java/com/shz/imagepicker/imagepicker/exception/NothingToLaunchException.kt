package com.shz.imagepicker.imagepicker.exception

import com.shz.imagepicker.imagepicker.ImagePicker

/**
 * Is thrown form [ImagePicker] when no picker was configuring to launch.
 *
 * To fix try to call useGallery(true) or useCamera(true) in [ImagePicker.Builder].
 *
 * @see ImagePicker.Builder
 */
class NothingToLaunchException : Throwable("Nothing to launch. Make sure that you have set to true useGallery() or useCamera().")
