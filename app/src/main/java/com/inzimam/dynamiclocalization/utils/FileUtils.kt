package com.inzimam.dynamiclocalization.utils

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class FileUtils {
    private val fileName = "language.csv"
    private val BASE_URL = "https://inzimam.pythonanywhere.com/return-files/"
    fun getFile(): File {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return File(folder, fileName)
    }

    fun getInputStreamReader(): InputStreamReader {
        return InputStreamReader(FileInputStream(getFile()))
    }

    fun getUrl(): String {
        return BASE_URL + fileName
    }
}