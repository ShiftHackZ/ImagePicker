package com.shz.imagepicker.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class ImagePath {

    private static final String FILE_EXTENSION_JPEG = ".jpeg";
    private static final String FILE_EXTENSION_JPG = ".jpg";
    private static final String FILE_SCHEMA_CONTENT = "content:";

    public static Uri getCaptureImageOutputUri(Context context, String filename) {
        Uri outputUri = null;
        File imageStore = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (imageStore != null) {
            outputUri = Uri.fromFile(new File(imageStore.getPath(), filename + FILE_EXTENSION_JPEG));
        }
        return outputUri;
    }

    public static Uri getCaptureImageResultUri(Context context, Intent data, String filename) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE;
        }
        if (isCamera || data.getData() == null) {
            return getCaptureImageOutputUri(context, filename);
        } else {
            return data.getData();
        }
    }

    public static Uri getNormalizedUri(Context context, Uri uri) {
        if (uri != null && uri.toString().contains(FILE_SCHEMA_CONTENT)) {
            return Uri.fromFile(getPath(context, uri, MediaStore.Images.Media.DATA));
        } else {
            return uri;
        }
    }

    public static String getImagePathFromInputStreamUri(Context context, Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File photoFile = createTempFileFrom(context, inputStream);
                filePath = photoFile.getPath();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return filePath;
    }

    private static File createTempFileFrom(Context context, InputStream inputStream) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTempFile(context, null);
            FileOutputStream outputStream = new FileOutputStream(targetFile);

            while (true) {
                read = inputStream.read(buffer);
                if (read == -1) {
                    break;
                }
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return targetFile;
    }

    private static File createTempFile(Context context, String filePath) {
        String tempFilename;
        if (filePath == null) {
            tempFilename = String.valueOf(Calendar.getInstance().getTimeInMillis());
        } else {
            tempFilename = filePath;
        }
        return new File(context.getExternalCacheDir(), tempFilename + FILE_EXTENSION_JPG);
    }

    private static File getPath(Context context, Uri uri, String column) {
        String[] columns = { column };
        Cursor cursor = context.getContentResolver().query(uri, columns, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(column);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return new File(path);
    }
}
