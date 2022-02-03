package com.jisulim.snackcart.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrefUtil @Inject constructor(
        @ApplicationContext private val context: Context
    ) {

    val prefs = context.getSharedPreferences("pref_key", Context.MODE_PRIVATE)

//    val COOKIE = "cookie"

    fun <T : Any?> getValue(key: String, defaultValue: T) = when (defaultValue) {
        is Int -> prefs.getInt(key, defaultValue)
        is Boolean -> prefs.getBoolean(key, defaultValue)
        is Float -> prefs.getFloat(key, defaultValue)
        is Long -> prefs.getLong(key, defaultValue)
        else -> prefs.getString(key, defaultValue.toString())
    }

    fun getIntValue(key: String, defaultValue: Int) = prefs.getInt(key, defaultValue)
    fun getBooleanValue(key: String, defaultValue: Boolean) = prefs.getBoolean(key, defaultValue)
    fun getFloatValue(key: String, defaultValue: Float) = prefs.getFloat(key, defaultValue)
    fun getLongValue(key: String, defaultValue: Long) = prefs.getLong(key, defaultValue)
    fun getStringValue(key: String, defaultValue: String?) = prefs.getString(key, defaultValue)
}