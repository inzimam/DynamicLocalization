package com.inzimam.dynamiclocalization

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.inzimam.dynamiclocalization.utils.AllLanguage
import com.inzimam.dynamiclocalization.utils.Data
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStreamReader

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private var myApp: Application = app
    private var msg: String = ""
    private var lastMsg = ""
    private val msgLiveData = MutableLiveData<Data>()

    internal fun getMsg(): MutableLiveData<Data> {
        return msgLiveData
    }


    internal fun downloadCSV(url: String) {
        val directory = File(Environment.DIRECTORY_DOWNLOADS)
        if (!directory.isDirectory) directory.mkdir()
        val downloadManager = myApp.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }
        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        delay(1000)
                        downloading = false
                    }
                    msg = statusMessage(url, directory, status)
                    if (msg != lastMsg) {
                        msgLiveData.postValue(Data(msg, status))
                        lastMsg = msg
                    }
                    cursor.close()
                }
            }
        }
    }

    private fun statusMessage(url: String, directory: File, status: Int): String {
        return when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "File downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
    }

    internal fun parseCSV(isr: InputStreamReader) {
        try {
            val reader = CSVReader(isr)
            var nextLine: Array<String>
            while (reader.readNext().also { nextLine = it } != null) {
                if (AllLanguage.containsKey(nextLine[0])) {
                    AllLanguage.setKeyValue(nextLine[0], nextLine[1], nextLine[2])
                } else {
                    AllLanguage.setLanguageValue(nextLine[0], nextLine[1], nextLine[2])
                }
                println(nextLine[0] + " " + nextLine[1] + " " + nextLine[2])
            }
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }
}