package com.adiluhung.mobilejournaling.ui.screen.authed.moodCheckIn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.GetUserProfileResponse
import com.adiluhung.mobilejournaling.ui.common.UiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoodCheckInViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _uiState = MutableLiveData<UiState<String>>(UiState.Empty)
   val uiState: LiveData<UiState<String>>
      get() = _uiState

   private fun getProfile() {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
      client.enqueue(object: Callback<GetUserProfileResponse>{
         override fun onResponse(
            call: Call<GetUserProfileResponse>,
            response: Response<GetUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _uiState.value = UiState.Success(resBody.data.firstName)
               }
            } else {
               Log.e("MoodCheckInViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
            Log.e("MoodCheckInViewModel", "Error: ${t.message}")
         }
      })
   }

   init {
      getProfile()
   }
}