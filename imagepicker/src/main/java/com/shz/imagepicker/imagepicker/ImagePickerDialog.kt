package com.shz.imagepicker.imagepicker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment

class ImagePickerDialog(private val listener: ImagePickerDialogListener) : DialogFragment() {

    interface ImagePickerDialogListener {
        fun onCamera()
        fun onGallery()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            //dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            val params = dialog.window!!.attributes
            //params.horizontalMargin = 0.01f;
            dialog.window!!.attributes = params
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.layout_image_picker, container, false)
        (rootView.findViewById<View>(R.id.btn_camera) as LinearLayout).setOnClickListener {
            listener.onCamera()
            dismissAllowingStateLoss()
        }
        (rootView.findViewById<View>(R.id.btn_gallery) as LinearLayout).setOnClickListener {
            listener.onGallery()
            dismissAllowingStateLoss()
        }
        return rootView
    }
}
