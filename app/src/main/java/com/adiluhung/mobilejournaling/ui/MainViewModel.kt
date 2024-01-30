package com.adiluhung.mobilejournaling.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adiluhung.mobilejournaling.data.UserPreferences

class MainViewModel(private val pref: UserPreferences): ViewModel() {
    fun getLoggedInUser() : LiveData<String?> = pref.getUserToken().asLiveData()
}