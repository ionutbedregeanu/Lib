package com.lib.data

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefsHelper @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        const val FIRST_REQUEST_KEY = "FIRST_REQUEST"
    }

    fun setFirstRequestValue(value: Boolean) {
        sharedPreferences.edit().putBoolean(FIRST_REQUEST_KEY, value).apply()
    }

    fun getFirstRequestValue() = sharedPreferences.getBoolean(FIRST_REQUEST_KEY, true)

}
