package com.inzimam.dynamiclocalization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.inzimam.dynamiclocalization.databinding.ActivityMainBinding
import com.inzimam.dynamiclocalization.utils.AllLanguage
import com.inzimam.dynamiclocalization.utils.LANGUAGES
import com.opencsv.CSVReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        parseCSV()
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

    private fun parseCSV() {
        try {
            val reader = CSVReader(InputStreamReader(resources.openRawResource(R.raw.language)))
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