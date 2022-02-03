package com.jisulim.snackcart.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var _fragmentStatus = MutableLiveData<Int>(1)

    val fragmentStatus: LiveData<Int>
        get() = _fragmentStatus

    fun updateFragmentStatus(id: Int) {
        _fragmentStatus.value = id
    }

}