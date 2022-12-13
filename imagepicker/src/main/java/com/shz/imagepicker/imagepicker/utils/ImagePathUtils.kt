package com.shz.imagepicker.imagepicker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.*
import java.lang.Exception
import java.util.*
import kotlin.Throws


private const val FILE_EXTENSION_JPEG = ".jpeg"
private const val FILE_EXTENSION_JPG = ".jpg"
private const val FILE_SCHEMA_CONTENT = "content:"


fun getCaptureImageOutputUri(context: Context?, filename: String): Uri? = context
    ?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    ?.let { file -> Uri.fromFile(File(file.path, filename + FILE_EXTENSION_JPEG)) }


fun getCaptureImageResultUri(context: Context, data: Intent?, filename: String): Uri? {
    var isCamera = true
    if (data != null && data.data != null) {
        val action = data.action
        isCamera = action != null && action === MediaStore.ACTION_IMAGE_CAPTURE
    }
    return if (isCamera || data!!.data == null) getCaptureImageOutputUri(context, filename)
    else data.data
}


fun getNormalizedUri(context: Context, uri: Uri?): Uri? =
    if (uri != null && uri.toString().contains(FILE_SCHEMA_CONTENT))
        Uri.fromFile(
            getPath(
                context,
                uri,
                MediaStore.Images.Media.DATA
            )
        )
    else uri


fun getImagePathFromInputStreamUri(context: Context, uri: Uri): String? {
    var inputStream: InputStream? = null
    var filePath: String? = null
    if (uri.authority != null) {
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            val photoFile = createTempFileFrom(context, inputStream)
            filePath = photoFile!!.path
        } catch (ex: FileNotFoundException) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            try {
                inputStream!!.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
    return filePath
}

@Throws(IOException::class)
private fun createTempFileFrom(context: Context, inputStream: InputStream?): File? {
    var targetFile: File? = null
    if (inputStream != null) {
        var read: Int
        val buffer = ByteArray(8 * 1024)
        targetFile = createTempFile(context, null)
        val outputStream = FileOutputStream(targetFile)
        while (true) {
            read = inputStream.read(buffer)
            if (read == -1) {
                break
            }
            outputStream.write(buffer, 0, read)
        }
        outputStream.flush()
        try {
            outputStream.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
    return targetFile
}

private fun createTempFile(context: Context, filePath: String?): File {
    val tempFilename: String = filePath ?: Calendar.getInstance().timeInMillis.toString()
    return File(context.externalCacheDir, tempFilename + FILE_EXTENSION_JPG)
}

private fun getPath(context: Context, uri: Uri, column: String): File {
    val columns = arrayOf(column)
    val cursor = context.contentResolver.query(uri, columns, null, null, null)
    val columnIndex = cursor!!.getColumnIndexOrThrow(column)
    cursor.moveToFirst()
    val path = cursor.getString(columnIndex)
    cursor.close()
    return File(path)
}

