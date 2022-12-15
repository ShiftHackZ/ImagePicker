package com.shz.imagepicker.imagepicker.exception

import com.shz.imagepicker.imagepicker.ImagePicker

private const val DEFAULT_NOT_SUPPORTED_EXCEPTION_MESSAGE =
    "This feature is not supported on your configuration device"

/**
 * Is thrown form [ImagePicker] when some feature is not supported.
 */
class FeatureNotSupportedException(
    override val message: String = DEFAULT_NOT_SUPPORTED_EXCEPTION_MESSAGE,
) : Throwable()
