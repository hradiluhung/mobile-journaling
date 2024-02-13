package com.adiluhung.mobilejournaling.ui.screen.authed.detailSession

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.CompleteSessionResponse
import com.adiluhung.mobilejournaling.data.network.responses.SessionDetailResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteSessionResponse
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

class DetailSessionViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _uiState = MutableLiveData<UiState<SessionDetailResponse>>(UiState.Empty)
   val uiState: LiveData<UiState<SessionDetailResponse>>
      get() = _uiState

   private val _updateState = MutableLiveData<UiState<CompleteSessionResponse>>(UiState.Empty)
   val updateState: LiveData<UiState<CompleteSessionResponse>>
      get() = _updateState

   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   fun updateFavoriteSession(id: Int) {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().updateFavoriteSession("Bearer $token", id)
      client.enqueue(object : Callback<UpdateFavoriteSessionResponse> {
         override fun onResponse(
            call: Call<UpdateFavoriteSessionResponse>,
            response: Response<UpdateFavoriteSessionResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  sendMessage(resBody.message)
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<UpdateFavoriteSessionResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   fun getDetailSession(id: String) {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().getDetailSession("Bearer $token", id)
      client.enqueue(object : Callback<SessionDetailResponse>{
         override fun onResponse(
            call: Call<SessionDetailResponse>,
            response: Response<SessionDetailResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _uiState.value = UiState.Success(resBody)
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<SessionDetailResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   fun completeSession(id: String) {
      _updateState.value = UiState.Loading
      val client = ApiConfig.getApiService().completeSession("Bearer $token", id, currentDate)
      client.enqueue(object : Callback<CompleteSessionResponse>{
         override fun onResponse(
            call: Call<CompleteSessionResponse>,
            response: Response<CompleteSessionResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _updateState.value = UiState.Success(resBody)
               }
            } else {
               Log.e("DetailSessionViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<CompleteSessionResponse>, t: Throwable) {
            Log.e("DetailSessionViewModel", "Error: ${t.message}")
         }
      })
   }
}