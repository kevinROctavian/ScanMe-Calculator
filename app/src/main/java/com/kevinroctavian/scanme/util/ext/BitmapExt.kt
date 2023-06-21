package com.kevinroctavian.scanme.util.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.File
import java.io.FileOutputStream


fun Bitmap?.saveImage(context: Context): String? {
    val directory = File(context.cacheDir, "images")
    directory.mkdirs()
    val file = File.createTempFile(
        "ScanMe-",
        ".jpg",
        directory,
    )
    if (file.exists()) file.delete()
    try {
        val out = FileOutputStream(file)
        this?.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return file.path
}

fun Bitmap?.rotateImage(angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return this?.let { Bitmap.createBitmap(it, 0, 0, this.width , this.height, matrix, true) }
}
