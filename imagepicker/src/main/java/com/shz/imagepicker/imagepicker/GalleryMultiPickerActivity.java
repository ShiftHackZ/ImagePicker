package com.shz.imagepicker.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;

public class GalleryMultiPickerActivity extends Activity {

    public static ImagePickerCallback mCallback;

    private static final int PERMISSION_REQUEST_READ_STORAGE = 54564;
    private static final int IMAGE_REQUEST_GALLERY = 54565;

    private static final String GALLERY_IMAGE_MIME = "image/jpeg";

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryPicker();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkGalleryPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_GALLERY) {
            if (data != null) {
                ArrayList<File> files = new ArrayList<>();

                /*if (data.getData() != null) {
                    Log.d("MultiPicker", "data: " + data.getData().toString());
                    String filename = ImagePath.getImagePathFromInputStreamUri(this, data.getData());
                    File file = new File(filename);
                    if (file.exists()) {
                        files.add(file);
                    }
                }*/
                if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        String filename = ImagePath.getImagePathFromInputStreamUri(this, item.getUri());
                        File file = new File(filename);
                        if (file.exists()) {
                            files.add(file);
                        }
                    }
                }

                mCallback.onImagesSelected(files);
            }
        }
        finish();
    }

    private void checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    PERMISSION_REQUEST_READ_STORAGE
            );
        } else {
            startGalleryPicker();
        }
    }

    private void startGalleryPicker() {
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        }
//        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, GALLERY_IMAGE_MIME);
//        startActivityForResult(galleryIntent, IMAGE_REQUEST_GALLERY);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
//**The following line is the important one!
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), IMAGE_REQUEST_GALLERY); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult
    }
}