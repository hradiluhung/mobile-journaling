package com.adiluhung.mobilejournaling.ui.screen.authed.reminder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import kotlinx.coroutines.launch

class ReminderViewModel(
   private val pref: UserPreferences
) : ViewModel() {
   private val _alarmTime = MutableLiveData<String?>()
   val alarmTime: LiveData<String?> = _alarmTime

   fun saveAlarm(time: String?) {
      viewModelScope.launch {
         pref.saveAlarm(time ?: "")
      }
   }

   private fun getAlarm() {
      viewModelScope.launch {
         pref.getAlarm().collect { alarm ->
            _alarmTime.value = alarm
            Log.d("ReminderViewModel", "Alarm time: $alarm")
         }
      }
   }

   fun deleteAlarm() {
      viewModelScope.launch {
         pref.saveAlarm("")
      }
   }

   init {
      getAlarm()
   }
}
