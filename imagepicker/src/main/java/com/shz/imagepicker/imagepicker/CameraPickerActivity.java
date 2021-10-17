package com.shz.imagepicker.imagepicker;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class CameraPickerActivity extends Activity {

    public static ImagePickerCallback mCallback;

    private static final int PERMISSION_REQUEST_CAMERA = 54560;
    private static final int IMAGE_REQUEST_CAMERA = 54561;

    private static final String FILE_PROVIDER_PREFIX = ".provider";

    private String mFilename;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraPicker();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CAMERA) {
            ArrayList<File> files = new ArrayList<>();
            Uri uri = ImagePath.getCaptureImageResultUri(this, data, mFilename);
            Uri uriFile = ImagePath.getNormalizedUri(this, uri);
            File file = new File(uriFile.getPath());
            files.add(file);
            mCallback.onImagesSelected(files);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.CAMERA },
                    PERMISSION_REQUEST_CAMERA
            );
        } else {
            startCameraPicker();
        }
    }

    private void startCameraPicker() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFilename = String.valueOf(System.nanoTime());
        Uri uri = ImagePath.getCaptureImageOutputUri(this, mFilename);
        if (uri != null) {
            File file = new File(uri.getPath());
            if (Build.VERSION.SDK_INT >= 24) {
                cameraIntent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(
                                this,
                                this.getPackageName() + FILE_PROVIDER_PREFIX,
                                file
                        )
                );
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            startActivityForResult(cameraIntent, IMAGE_REQUEST_CAMERA);
        }
    }
}