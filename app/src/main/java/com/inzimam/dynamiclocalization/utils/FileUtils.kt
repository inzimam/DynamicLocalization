package com.inzimam.dynamiclocalization.utils

import android.os.Environment
import java.io.File

class FileUtils {
    public fun getFile(): File {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return File(folder, "language.csv")
    }
}