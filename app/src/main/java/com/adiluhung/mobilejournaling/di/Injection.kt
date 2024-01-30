package com.adiluhung.mobilejournaling.di

import android.content.Context
import com.adiluhung.mobilejournaling.data.UserPreferences

object Injection {
    fun provideUserPreference(context: Context): UserPreferences {
        return UserPreferences(context)
    }
}