package com.shz.imagepicker.imagepicker

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val PERMISSION_CHECK_REQUEST = 54567

fun Activity.checkGalleryPermission(action: () -> Unit) {
    val hasNoReadPermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_DENIED

    val hasNoWritePermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_DENIED

    if (hasNoReadPermission || hasNoWritePermission)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ),
            PERMISSION_CHECK_REQUEST,
        )
    else action()
}
