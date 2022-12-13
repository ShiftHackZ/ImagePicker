package com.shz.imagepicker.imagepicker.model

import android.content.Intent
import java.io.Serializable

/**
 * Describes all available types of Gallery Pickers.
 *
 * Implements [Serializable].
 */
enum class GalleryPicker : Serializable {
    /**
     * Matches system Gallery Picker that is implemented in Android ROM.
     */
    NATIVE,

    /**
     * Matches Custom Gallery Picker that is implemented in this library.
     *
     * The reason to use [CUSTOM] picker is that many of old and non-AOSP Android ROMs
     * do not respect [Intent.EXTRA_ALLOW_MULTIPLE] that is passed to launch [Intent].
     *
     * Advice: Use this mainly when you need to support multiple images selection on custom ROMs.
     */
    @Deprecated("GalleryPicker.CUSTOM is deprecated from Android 13 (SDK 33)")
    CUSTOM;
}
