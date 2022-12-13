package com.shz.imagepicker.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shz.imagepicker.imagepicker.util.FileOrientationHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GallerySinglePickerActivity extends Activity {

    public static ImagePickerCallback mCallback;

    private static final int PERMISSION_REQUEST_READ_STORAGE = 54562;
    private static final int IMAGE_REQUEST_GALLERY = 54563;

    private static final String GALLERY_IMAGE_MIME = "image/jpeg";
    private FileOrientationHandler fileOrientationHandler = new FileOrientationHandler();


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_GALLERY) {
            if (data != null && data.getData() != null) {
                ArrayList<File> files = new ArrayList<>();
                String filename = ImagePath.getImagePathFromInputStreamUri(this, data.getData());
                Log.d("SinglePicker", "data: " + data.getData().toString());
                File file = new File(filename);
                if (file.exists()) {

                    try{
                        fileOrientationHandler.rotateAndReWriteImageFile(file);
                    }catch (IOException e){
                        Log.d(this.getClass().getSimpleName(),"Error rotation image "+e.getLocalizedMessage());
                    }

                    files.add(file);
                    mCallback.onImagesSelected(files);
                }
            }
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkGalleryPermission();
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, GALLERY_IMAGE_MIME);
        startActivityForResult(galleryIntent, IMAGE_REQUEST_GALLERY);
    }
}