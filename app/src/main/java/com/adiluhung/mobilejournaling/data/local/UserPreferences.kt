package com.adiluhung.mobilejournaling.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
   fun getUserToken(): Flow<String?> {
      return context.dataStore.data.map { preferences ->
         preferences[TOKEN_KEY]
      }
   }

   suspend fun saveUserToken(token: String) {
      context.dataStore.edit { preference ->
         preference[TOKEN_KEY] = token
      }
   }

   suspend fun deleteUserToken() {
      context.dataStore.edit { preference ->
         preference.remove(TOKEN_KEY)
      }
   }

   fun getCompleteProfile(): Flow<Boolean> {
      return context.dataStore.data.map { preferences ->
         preferences[COMPLETE_PROFILE_KEY] ?: false
      }
   }

   suspend fun saveCompleteProfile(complete: Boolean) {
      context.dataStore.edit { preference ->
         preference[COMPLETE_PROFILE_KEY] = complete
      }
   }

   fun getAlarm(): Flow<String?> {
      return context.dataStore.data.map { preferences ->
         preferences[ALARM_KEY]
      }
   }

   suspend fun saveAlarm(alarm: String) {
      context.dataStore.edit { preference ->
         preference[ALARM_KEY] = alarm
      }
   }

   companion object {
      private val TOKEN_KEY = stringPreferencesKey("token")
      private val COMPLETE_PROFILE_KEY = booleanPreferencesKey("complete_profile")
      private val ALARM_KEY = stringPreferencesKey("alarm")
      private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
   }
}