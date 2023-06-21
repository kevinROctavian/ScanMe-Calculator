package com.kevinroctavian.scanme.util.provider

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.kevinroctavian.scanme.R
import java.io.File

class ScanMeFileProvider : FileProvider(
    R.xml.file_paths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "ScanMe-",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}