package com.adiluhung.mobilejournaling.ui.screen.authed.profile

import android.net.Uri
import android.util.Log
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }
   private val _alarmTime = MutableLiveData<String?>()
   val alarmTime: LiveData<String?> = _alarmTime

   private val _userData = MutableLiveData<UiState<GetUserProfileResponse>>(UiState.Empty)
   val userData: MutableLiveData<UiState<GetUserProfileResponse>>
      get() = _userData

   private val _updatePhoto = MutableLiveData<UiState<String>>(UiState.Empty)
   val updatePhoto: MutableLiveData<UiState<String>>
      get() = _updatePhoto

   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   fun logout() {
      viewModelScope.launch {
         pref.deleteUserToken()
      }
   }

   private fun getProfileUser() {
      _userData.value = UiState.Loading
      val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
      client.enqueue(object : Callback<GetUserProfileResponse> {
         override fun onResponse(
            call: Call<GetUserProfileResponse>,
            response: Response<GetUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               _userData.value = UiState.Success(resBody)
            } else {
               _userData.value = UiState.Error(response.message())
            }
         }

         override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
            _userData.value = UiState.Error(t.message.toString())
         }
      })
   }

   fun uploadImage(file: File) {
      _updatePhoto.value = UiState.Loading


      val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
      val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
         "file",
         file.name,
         requestImageFile
      )

      val client = ApiConfig.getApiService().updateProfilePicture("Bearer $token", imageMultipart)
      client.enqueue(object : Callback<UpdateUserProfileResponse> {
         override fun onResponse(
            call: Call<UpdateUserProfileResponse>,
            response: Response<UpdateUserProfileResponse>
         ) {
            Log.d("ProfileViewModel", "onResponse: $response")
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  sendMessage(resBody.message)
                  _updatePhoto.value = UiState.Success(resBody.message)
                  getProfileUser()
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
               _updatePhoto.value = UiState.Error(errorMessage)
            }
         }

         override fun onFailure(call: Call<UpdateUserProfileResponse>, t: Throwable) {
            sendMessage(t.message.toString())
            _updatePhoto.value = UiState.Error(t.message.toString())
            Log.e("ProfileViewModel", "onFailure: ${t.message.toString()}")
         }
      })
   }

   private fun getAlarm() {
      viewModelScope.launch {
         pref.getAlarm().collect { alarm ->
            _alarmTime.value = alarm
            Log.d("ReminderViewModel", "Alarm time: $alarm")
         }
      }
   }

   init {
      getProfileUser()
      getAlarm()
   }

}