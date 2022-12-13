package com.shz.imagepicker.imagepicker.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU, lambda = 2)
fun Activity.checkReadExternalStoragePermission(request: Int, action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= 33) action()
    else {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ),
                request,
            )
        } else {
            action()
        }
    }
}

fun Activity.checkCameraPermission(request: Int, action: () -> Unit) {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_DENIED
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            request,
        )
    } else action()
}
