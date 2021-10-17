package com.shz.imagepicker.imagepicker;

import android.app.Activity;
import android.content.Intent;

public class ImagePicker {

    private static final String IMAGE_PICKER_DIALOG = "ImagePicker";

    private Activity mActivity;
    private ImagePickerCallback mCallback;
    private boolean mIsUsingCamera = false;
    private boolean mIsUsingGallery = false;
    private boolean mIsUsingGalleryMultiSelect = false;

    private ImagePicker() { }

    public static class Builder {
        private final ImagePicker mPicker;

        public Builder(Activity activity, ImagePickerCallback callback) {
            mPicker = new ImagePicker();
            mPicker.mActivity = activity;
            mPicker.mCallback = callback;
        }

        public Builder useCamera(boolean isUsingCamera) {
            mPicker.mIsUsingCamera = isUsingCamera;
            return this;
        }

        public Builder useGallery(boolean isUsingGallery) {
            mPicker.mIsUsingGallery = isUsingGallery;
            return this;
        }

        public Builder useMultiSelection(boolean isUsingMultiSelection) {
            mPicker.mIsUsingGalleryMultiSelect = isUsingMultiSelection;
            return this;
        }

        public ImagePicker build() {
            return mPicker;
        }
    }

    public void start() {
        if (mIsUsingCamera && !mIsUsingGallery) {
            startCamera();
        } else if (mIsUsingGallery && !mIsUsingGalleryMultiSelect && !mIsUsingCamera) {
            startGallerySingle();
        } else if (mIsUsingGallery && mIsUsingGalleryMultiSelect && !mIsUsingCamera) {
            startGalleryMultiple();
        } else if (mIsUsingGallery && !mIsUsingGalleryMultiSelect && mIsUsingCamera) {
            ImagePickerDialog dialog = new ImagePickerDialog(new ImagePickerDialog.ImagePickerDialogListener() {
                @Override
                public void onCamera() {
                    startCamera();
                }

                @Override
                public void onGallery() {
                    startGallerySingle();
                }
            });
            dialog.show(mActivity.getFragmentManager(), IMAGE_PICKER_DIALOG);
        } else if (mIsUsingGallery && mIsUsingGalleryMultiSelect && mIsUsingCamera) {
            ImagePickerDialog dialog = new ImagePickerDialog(new ImagePickerDialog.ImagePickerDialogListener() {
                @Override
                public void onCamera() {
                    startCamera();
                }

                @Override
                public void onGallery() {
                    startGalleryMultiple();
                }
            });
            dialog.show(mActivity.getFragmentManager(), IMAGE_PICKER_DIALOG);
        }
    }

    private void startCamera() {
        CameraPickerActivity.mCallback = mCallback;
        mActivity.startActivity(new Intent(mActivity, CameraPickerActivity.class));
    }

    private void startGallerySingle() {
        GallerySinglePickerActivity.mCallback = mCallback;
        mActivity.startActivity(new Intent(mActivity, GallerySinglePickerActivity.class));
    }

    private void startGalleryMultiple() {
        GalleryMultiPickerActivity.mCallback = mCallback;
        mActivity.startActivity(new Intent(mActivity, GalleryMultiPickerActivity.class));
    }
}
