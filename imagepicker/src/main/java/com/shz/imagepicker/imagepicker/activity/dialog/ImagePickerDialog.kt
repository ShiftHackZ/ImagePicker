package com.shz.imagepicker.imagepicker.activity.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.shz.imagepicker.imagepicker.R

internal class ImagePickerDialog(private val listener: Listener) : DialogFragment() {

    interface Listener {
        fun launchCamera()
        fun launchGallery()
        fun onDismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener.onDismiss()
        super.onDismiss(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.image_picker_dialog_selection, container, false).apply {
        findViewById<LinearLayout>(R.id.btn_camera)?.setOnClickListener {
            listener.launchCamera()
            dismissAllowingStateLoss()
        }
        findViewById<LinearLayout>(R.id.btn_gallery)?.setOnClickListener {
            listener.launchGallery()
            dismissAllowingStateLoss()
        }
    }
}
