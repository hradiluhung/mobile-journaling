package com.adiluhung.mobilejournaling.ui.screen.guest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.AuthResponse
import com.adiluhung.mobilejournaling.ui.common.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: UserPreferences) : ViewModel() {
   private val _uiState = MutableLiveData<UiState<String>>(UiState.Empty)
   val uiState: LiveData<UiState<String>>
      get() = _uiState


   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   fun login(email: String, password: String) {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().login(email, password)
      client.enqueue(object : Callback<AuthResponse> {
         override fun onResponse(
            call: Call<AuthResponse>,
            response: Response<AuthResponse>
         ) {
            if (response.isSuccessful) {
               val data = response.body()
               if (data != null) {
                  _uiState.value = UiState.Success(data.message)
                  sendMessage(data.message)

                  viewModelScope.launch {
                     data.data.token.let { pref.saveUserToken(it) }
                  }
               }
            } else {
               val errorMessage = response.errorBody()?.string().let {
                  if (it != null) {
                     JSONObject(it).getString("message")
                  } else {
                     "Unknown Error"
                  }
               }
               sendMessage(errorMessage)
               _uiState.value = UiState.Error(errorMessage)
            }
         }

         override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
            _uiState.value = UiState.Error(t.message ?: "Unknown error")
         }
      })
   }

   fun updateCompleteProfile(value: Boolean) {
      viewModelScope.launch {
         pref.saveCompleteProfile(value)
      }
   }

   fun register(
      email: String,
      password: String,
      firstName: String,
      lastName: String
   ) {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().register(
         email,
         password,
         firstName,
         lastName
      )
      client.enqueue(object : Callback<AuthResponse> {
         override fun onResponse(
            call: Call<AuthResponse>,
            response: Response<AuthResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               _uiState.value = UiState.Success(resBody?.message)
               sendMessage(resBody?.message ?: "")

               updateCompleteProfile(false)
               viewModelScope.launch {
                  resBody?.data?.token?.let { pref.saveUserToken(it) }
               }
            } else {
               val errorMessage = response.errorBody()?.string().let {
                  if (it != null) {
                     JSONObject(it).getString("message")
                  } else {
                     "Unknown Error"
                  }
               }
               sendMessage(errorMessage)
               _uiState.value = UiState.Error(errorMessage)
            }
         }

         override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
            _uiState.value = UiState.Error(t.message ?: "Unknown error")
         }
      })
   }

}