package com.shz.imagepicker.imagepicker.exception

import com.shz.imagepicker.imagepicker.ImagePicker

/**
 * Is thrown in some memory leak use cases when launching interactive dialog.
 *
 * To fix try to pass Activity context to [ImagePicker.launch] method.
 *
 * @see ImagePicker.launch
 */
class UnableLaunchDialogException : Throwable("Unable to launch dialog, make sure you are passing Activity context")
