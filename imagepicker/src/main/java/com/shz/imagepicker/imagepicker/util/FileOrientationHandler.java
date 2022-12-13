package com.shz.imagepicker.imagepicker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOrientationHandler {


    public void rotateAndReWriteImageFile(File file) throws IOException {

        // Bitmap is rotated according to camera orientation
        rotateImageIfRequired(file);
    }


    private   void rotateImageIfRequired(File file) throws IOException{
        ExifInterface ei = new ExifInterface(file.getAbsolutePath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        Bitmap rotatedBitmap;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                saveBitmap(rotatedBitmap,file);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                saveBitmap(rotatedBitmap,file);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                saveBitmap(rotatedBitmap,file);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
        }

    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void saveBitmap(Bitmap rotatedBitmap,File file) throws IOException{
        FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    }


}
