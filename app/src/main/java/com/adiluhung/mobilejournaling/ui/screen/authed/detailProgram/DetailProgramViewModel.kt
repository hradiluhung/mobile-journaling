package com.adiluhung.mobilejournaling.ui.screen.authed.detailProgram

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.ProgramDetailResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteSessionResponse
import com.adiluhung.mobilejournaling.ui.common.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProgramViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _uiState = MutableLiveData<UiState<ProgramDetailResponse>>(UiState.Empty)
   val uiState: LiveData<UiState<ProgramDetailResponse>>
      get() = _uiState

   private val _program = MutableLiveData<ProgramDetailResponse?>()

   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   fun getDetailProgram(id: String) {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().getDetailProgram("Bearer $token", id)
      client.enqueue(object : Callback<ProgramDetailResponse> {
         override fun onResponse(
            call: Call<ProgramDetailResponse>,
            response: Response<ProgramDetailResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _uiState.value = UiState.Success(resBody)
                  _program.value = resBody
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<ProgramDetailResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   fun updateFavoriteProgram(id: Int) {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().updateFavoriteProgram("Bearer $token", id)
      client.enqueue(object : Callback<UpdateFavoriteProgramResponse> {
         override fun onResponse(
            call: Call<UpdateFavoriteProgramResponse>,
            response: Response<UpdateFavoriteProgramResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  sendMessage(resBody.message)

                  _program.value?.data?.isFavorite = !(_program.value?.data?.isFavorite ?: false)
                  _uiState.value = UiState.Success(_program.value)
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<UpdateFavoriteProgramResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
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

                  _program.value?.data?.modules?.forEach { module ->
                     module.sessions.forEach { session ->
                        if (session.id == id) {
                           session.isFavorite = !session.isFavorite
                        }
                     }
                  }

                  _uiState.value = UiState.Success(_program.value)
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
}