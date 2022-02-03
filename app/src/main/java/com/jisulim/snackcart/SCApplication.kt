package com.jisulim.snackcart

import android.app.Application
import com.jisulim.snackcart.utils.ResourceUtil
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class SCApplication : Application() {

    companion object {
        lateinit var resource: ResourceUtil
    }

    override fun onCreate() {
        super.onCreate()
//        CookieSyncManager.createInstance(this)

        resource = ResourceUtil(resources)
    }

}