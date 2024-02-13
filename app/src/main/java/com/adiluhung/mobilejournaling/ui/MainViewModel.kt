package com.adiluhung.mobilejournaling.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.ui.common.UiState
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreferences) : ViewModel() {
   // fun getLoggedInUser() : LiveData<String?> = pref.getUserToken().asLiveData()

   private val _token = MutableLiveData<String?>()
   val token: LiveData<String?> = _token

   private val _isProfileComplete = MutableLiveData<Boolean>()
   val isProfileComplete: LiveData<Boolean> = _isProfileComplete

   private val _uiState = MutableLiveData<UiState<String>>(UiState.Empty)
   val uiState: LiveData<UiState<String>>
      get() = _uiState

   init {
      observeToken()
   }

   private fun observeToken() {
      _uiState.value = UiState.Loading
      viewModelScope.launch {
         pref.getUserToken().collect { newToken ->
            _token.value = newToken
            _uiState.value = UiState.Success(newToken)
         }
      }
   }

   fun observeCompleteProfile() {
      viewModelScope.launch {
         pref.getCompleteProfile().collect { isComplete ->
            _isProfileComplete.value = isComplete
         }
      }
   }
}