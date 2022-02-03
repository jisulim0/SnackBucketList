package com.jisulim.snackcart.utils

import android.content.res.Resources

class ResourceUtil(private val resource: Resources) {

    fun getString(id: Int) = resource.getString(id)

    fun getStringArray(id: Int) = resource.getStringArray(id)

}