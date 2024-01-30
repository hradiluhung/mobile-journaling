package com.adiluhung.mobilejournaling.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    fun getUserToken(): Flow<String?>{
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveUserToken(token: String){
        context.dataStore.edit{ preference ->
            preference[TOKEN_KEY] = token
        }
    }

    suspend fun deleteUserToken(){
        context.dataStore.edit{ preference ->
            preference.remove(TOKEN_KEY)
        }
    }

    companion object{
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
    }
}