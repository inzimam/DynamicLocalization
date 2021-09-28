package com.inzimam.dynamiclocalization

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inzimam.dynamiclocalization.databinding.ActivityMainBinding
import com.inzimam.dynamiclocalization.utils.AllLanguage
import com.inzimam.dynamiclocalization.utils.Data
import com.inzimam.dynamiclocalization.utils.FileUtils
import com.inzimam.dynamiclocalization.utils.LANGUAGES
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var isr: InputStreamReader
    private lateinit var viewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getMsg().observe(this, {
            show(it)
        })
        if (FileUtils().getFile().exists()) {
            isr = InputStreamReader(FileInputStream(FileUtils().getFile()))
            viewModel.parseCSV(isr)
            mainBinding.progressCircular.visibility = View.GONE
        } else {
            viewModel.downloadCSV("https://inzimam.pythonanywhere.com/return-files/language.csv")
        }

        mainBinding.english.setOnClickListener {
            mainBinding.map = AllLanguage.getValue(LANGUAGES.ENGLISH.toString())
        }
        mainBinding.hindi.setOnClickListener {
            mainBinding.map = AllLanguage.getValue(LANGUAGES.HINDI.toString())
        }
        mainBinding.dutch.setOnClickListener {
            mainBinding.map = AllLanguage.getValue(LANGUAGES.DUTCH.toString())
        }
    }

    private fun show(data: Data) {
        this.runOnUiThread {
            Toast.makeText(this, data.msg, Toast.LENGTH_SHORT).show()
            if (data.status == DownloadManager.STATUS_SUCCESSFUL) {
                mainBinding.progressCircular.visibility = View.GONE
                try {
                    val isr = InputStreamReader(FileInputStream(FileUtils().getFile()))
                    viewModel.parseCSV(isr)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

}