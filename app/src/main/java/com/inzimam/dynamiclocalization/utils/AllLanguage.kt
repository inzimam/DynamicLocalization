package com.inzimam.dynamiclocalization.utils

object AllLanguage {
    private val languageMap = HashMap<String, HashMap<String, String>>()

    fun setLanguageValue(languageKey: String, key: String, value: String) {
        val hashMap = HashMap<String, String>()
        hashMap[key] = value
        languageMap[languageKey] = hashMap
    }

    fun setKeyValue(languageKey: String, key: String, value: String) {
        languageMap[languageKey]?.put(key, value)
    }

    fun getValue(key: String): HashMap<String, String>? {
        return languageMap[key]
    }

    fun containsKey(key: String): Boolean {
        return languageMap.containsKey(key)
    }
}