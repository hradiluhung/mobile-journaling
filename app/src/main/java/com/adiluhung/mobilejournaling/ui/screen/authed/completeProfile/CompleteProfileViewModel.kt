package com.adiluhung.mobilejournaling.ui.screen.authed.completeProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.GetUserProfileResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateUserProfileResponse
import com.adiluhung.mobilejournaling.ui.common.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompleteProfileViewModel(private val pref: UserPreferences) : ViewModel() {
   data class FullName(
      val firstName: String,
      val lastName: String
   )

   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _uiState = MutableLiveData<UiState<FullName>>(UiState.Empty)
   val uiState: LiveData<UiState<FullName>>
      get() = _uiState

   private val _updateState = MutableLiveData<UiState<String>>(UiState.Empty)
   val updateState: LiveData<UiState<String>>
      get() = _updateState

   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   fun updateCompleteProfile(value: Boolean) {
      viewModelScope.launch {
         pref.saveCompleteProfile(value)
      }
   }

   fun updateName(value: String, lastName: String) {
      _uiState.value = UiState.Success(FullName(value, lastName))
   }

   private fun getUserProfile() {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
      client.enqueue(object : Callback<GetUserProfileResponse> {
         override fun onResponse(
            call: Call<GetUserProfileResponse>,
            response: Response<GetUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               _uiState.value = UiState.Success(
                  FullName(
                     resBody?.data?.firstName ?: "",
                     resBody?.data?.lastName ?: ""
                  )
               )
            } else {
               onFailure(call, Throwable("Terjadi kesalahan"))
            }
         }

         override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
            _uiState.value = UiState.Error(t.message ?: "Unknown error")
         }
      })
   }

   fun updateProfile(
      firstName: String,
      lastName: String,
      age: Int,
      gender: String,
      schoolName: String
   ) {
      _updateState.value = UiState.Loading
      val client =
         ApiConfig.getApiService()
            .updateProfile("Bearer $token", firstName, lastName, age, gender, schoolName)
      client.enqueue(object : Callback<UpdateUserProfileResponse> {
         override fun onResponse(
            call: Call<UpdateUserProfileResponse>,
            response: Response<UpdateUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _updateState.value = UiState.Success(resBody.message)
                  sendMessage(resBody.message)
                  updateCompleteProfile(true)
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

         override fun onFailure(call: Call<UpdateUserProfileResponse>, t: Throwable) {
            _updateState.value = UiState.Error(t.message ?: "Unknown error")
         }
      })

   }


   init {
      getUserProfile()
   }
}