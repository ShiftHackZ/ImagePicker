package com.shz.imagepicker.imagepicker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

@SuppressLint("ValidFragment")
public class ImagePickerDialog extends DialogFragment {

    public interface ImagePickerDialogListener {
        void onCamera();
        void onGallery();
    }

    private ImagePickerDialogListener mListener;

    public ImagePickerDialog(ImagePickerDialogListener listener) {
        super();
        this.mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            //dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            //params.horizontalMargin = 0.01f;
            dialog.getWindow().setAttributes(params);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_image_picker, container, false);
        ((LinearLayout) rootView.findViewById(R.id.btn_camera)).setOnClickListener(v -> {
            mListener.onCamera();
            dismissAllowingStateLoss();
        });
        ((LinearLayout) rootView.findViewById(R.id.btn_gallery)).setOnClickListener(v -> {
            mListener.onGallery();
            dismissAllowingStateLoss();
        });
        return rootView;
    }
}
