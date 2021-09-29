package com.inzimam.dynamiclocalization

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
        initViewModel()
        observeDownloadingStatus()
        setupLanguageData()
        setClickListener()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun observeDownloadingStatus() {
        viewModel.getMsg().observe(this, {
            show(it)
        })
    }

    private fun setupLanguageData() {
        if (FileUtils().getFile().exists()) {
            enableButtons()
            isr = FileUtils().getInputStreamReader()
            viewModel.parseCSV(isr)
            mainBinding.progressCircular.visibility = View.GONE
        } else {
            viewModel.downloadCSV(FileUtils().getUrl())
        }
    }

    private fun setClickListener() {
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
        Toast.makeText(this, data.msg, Toast.LENGTH_SHORT).show()
        if (data.status == DownloadManager.STATUS_SUCCESSFUL) {
            enableButtons()
            mainBinding.progressCircular.visibility = View.GONE
            try {
                val isr = FileUtils().getInputStreamReader()
                viewModel.parseCSV(isr)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun enableButtons() {
        mainBinding.english.isEnabled = true
        mainBinding.hindi.isEnabled = true
        mainBinding.dutch.isEnabled = true
    }
}