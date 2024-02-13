package com.adiluhung.mobilejournaling.ui.screen.authed.moodAddNote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.AllTagsResponse
import com.adiluhung.mobilejournaling.data.network.responses.CheckInMoodResponse
import com.adiluhung.mobilejournaling.data.network.responses.Tag
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.utils.currentDate
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

class MoodAddNoteViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _uiState = MutableLiveData<UiState<List<Tag>>>()
   val uiState: LiveData<UiState<List<Tag>>>
      get() = _uiState

   private val _checkInMessage = MutableLiveData<UiState<String>>()
   val checkInMessage: LiveData<UiState<String>>
      get() = _checkInMessage

   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   private fun getAllTags() {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().getAllTags("Bearer $token")
      client.enqueue(object : Callback<AllTagsResponse> {
         override fun onResponse(call: Call<AllTagsResponse>, response: Response<AllTagsResponse>) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _uiState.value = UiState.Success(resBody.data)
               }
            } else {
               Log.e("MoodAddNoteViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<AllTagsResponse>, t: Throwable) {
            _uiState.value = t.message?.let { UiState.Error(it) }
         }
      })
   }

   fun checkInMood(
      mood: String,
      note: String,
      tags: List<Int>,
   ) {
      _checkInMessage.value = UiState.Loading
      val client =
         ApiConfig.getApiService()
            .createCheckInMood("Bearer $token", currentDate, mood, note, tags.toTypedArray())
      client.enqueue(object : Callback<CheckInMoodResponse> {
         override fun onResponse(
            call: Call<CheckInMoodResponse>,
            response: Response<CheckInMoodResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _checkInMessage.value = UiState.Success(resBody.message)
                  sendMessage(resBody.message)
               }
            } else {
               val errorMessage =
                  response.errorBody()?.string().let {
                     if (it != null) {
                        JSONObject(it).getString("message")
                     } else {
                        "Unknown Error"
                     }
                  }
               sendMessage(errorMessage)
               _checkInMessage.value = UiState.Error(errorMessage)
            }
         }

         override fun onFailure(call: Call<CheckInMoodResponse>, t: Throwable) {
            _checkInMessage.value = t.message?.let { UiState.Error(it) }
            sendMessage(t.message.toString())
         }
      })
   }

   init {
      getAllTags()
   }
}