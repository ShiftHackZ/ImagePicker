package com.shz.imagepicker.imagepickerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shz.imagepicker.imagepicker.ImagePicker;
import com.shz.imagepicker.imagepicker.ImagePickerCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImagePickerCallback {

    private ImageView mResultImage;
    private LinearLayout mResultContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultImage = (ImageView) findViewById(R.id.iv_result);
        mResultContainer = (LinearLayout) findViewById(R.id.ll_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onImagesSelected(List<File> files) {
        if (files.size() > 0) {
            if (files.size() == 1) {
                Glide.with(this)
                        .load(files.get(0))
                        .into(mResultImage);

                mResultContainer.setVisibility(View.GONE);
                mResultImage.setVisibility(View.VISIBLE);
            } else {
                mResultContainer.removeAllViews();
                for (int i = 0; i < files.size(); i++) {
                    ImageView imageView = new ImageView(this);

                    Glide.with(this)
                            .load(files.get(i))
                            .into(imageView);

                    mResultContainer.addView(imageView);
                    imageView.getLayoutParams().height = 300;
                    imageView.requestLayout();
                }
                mResultContainer.setVisibility(View.VISIBLE);
                mResultImage.setVisibility(View.GONE);
            }
        }
    }

    public void onCameraClick(View v) {
        new ImagePicker.Builder(this, this)
                .useCamera(true)
                .build()
                .start();
    }

    public void onGalleryMultipleClick(View v) {
        new ImagePicker.Builder(this, this)
                .useGallery(true)
                .useMultiSelection(true)
                .build()
                .start();
    }

    public void onGalleryClick(View v) {
        new ImagePicker.Builder(this, this)
                .useGallery(true)
                .build()
                .start();
    }

    public void onGenericMultipleClick(View v) {
        new ImagePicker.Builder(this, this)
                .useGallery(true)
                .useCamera(true)
                .useMultiSelection(true)
                .build()
                .start();
    }

    public void onGenericClick(View v) {
        new ImagePicker.Builder(this, this)
                .useGallery(true)
                .useCamera(true)
                .build()
                .start();
    }
}