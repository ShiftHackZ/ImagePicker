package com.shz.imagepicker.imagepickerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.shz.imagepicker.imagepicker.ImagePicker;
import com.shz.imagepicker.imagepicker.ImagePickerCallback;
import com.shz.imagepicker.imagepicker.PickerResult;

public class MainActivity extends AppCompatActivity implements ImagePickerCallback {

    private ImageView mResultImage;
    private LinearLayout mResultContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultImage = (ImageView) findViewById(R.id.iv_result);
        mResultContainer = (LinearLayout) findViewById(R.id.ll_container);

        findViewById(R.id.btn_gallery).setOnClickListener((v) -> onGalleryClick());
        findViewById(R.id.btn_gallery_multiple).setOnClickListener((v) -> onGalleryMultipleClick());
        findViewById(R.id.btn_camera).setOnClickListener((v) -> onCameraClick());
        findViewById(R.id.btn_generic).setOnClickListener((v) -> onGenericClick());
        findViewById(R.id.btn_generic_multiple).setOnClickListener((v) -> onGenericMultipleClick());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onImagePickerResult(@NonNull PickerResult result) {
        if (result instanceof PickerResult.Single) {
            Glide.with(this)
                        .load(((PickerResult.Single) result).getImage().getFile())
                        .into(mResultImage);

            mResultContainer.setVisibility(View.GONE);
            mResultImage.setVisibility(View.VISIBLE);
        }
        if (result instanceof PickerResult.Multiple) {
            mResultContainer.removeAllViews();
                for (int i = 0; i < ((PickerResult.Multiple) result).getImages().size(); i++) {
                    ImageView imageView = new ImageView(this);

                    Glide.with(this)
                            .load(((PickerResult.Multiple) result).getImages().get(i).getFile())
                            .into(imageView);

                    mResultContainer.addView(imageView);
                    imageView.getLayoutParams().height = 300;
                    imageView.requestLayout();
                }
                mResultContainer.setVisibility(View.VISIBLE);
                mResultImage.setVisibility(View.GONE);
        }
    }

    public void onCameraClick() {
        getImagePicker()
                .useCamera(true)
                .build()
                .launch(this);
    }

    public void onGalleryMultipleClick() {
        getImagePicker()
                .useGallery(true)
                .multipleSelection(true)
                .build()
                .launch(this);
    }

    public void onGalleryClick() {
        getImagePicker()
                .useGallery(true)
                .build()
                .launch(this);
    }

    public void onGenericMultipleClick() {
        getImagePicker()
                .useGallery(true)
                .useCamera(true)
                .multipleSelection(true)
                .build()
                .launch(this);
    }

    public void onGenericClick() {
        getImagePicker()
                .useGallery(true)
                .useCamera(true)
                .build()
                .launch(this);
    }

    private ImagePicker.Builder getImagePicker() {
        return new ImagePicker.Builder(this.getPackageName() + ".provider", this);
    }
}