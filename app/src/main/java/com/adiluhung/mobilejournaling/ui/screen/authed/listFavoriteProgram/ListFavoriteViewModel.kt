package com.adiluhung.mobilejournaling.ui.screen.authed.listFavoriteProgram

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.AllProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.FavoriteSessionsResponse
import com.adiluhung.mobilejournaling.data.network.responses.Program
import com.adiluhung.mobilejournaling.data.network.responses.SessionDetail
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteSessionResponse
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

class ListFavoriteViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _listFavoriteProgram = MutableLiveData<UiState<List<Program>>>(UiState.Empty)
   val listFavoriteProgram: LiveData<UiState<List<Program>>>
      get() = _listFavoriteProgram

   private val _listFavoriteSession = MutableLiveData<UiState<List<SessionDetail>>>(UiState.Empty)
   val listFavoriteSession: LiveData<UiState<List<SessionDetail>>>
      get() = _listFavoriteSession

   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   private fun getAllFavoritePrograms() {
      _listFavoriteProgram.value = UiState.Loading
      val client = ApiConfig.getApiService().getAllFavoritePrograms("Bearer $token")
      client.enqueue(object : Callback<AllProgramResponse> {
         override fun onResponse(
            call: Call<AllProgramResponse>,
            response: Response<AllProgramResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _listFavoriteProgram.value = UiState.Success(resBody.data)
               }

            } else {
               val errorMessage = response.errorBody()?.string().let {
                  if (it != null) {
                     JSONObject(it).getString("message")
                  } else {
                     "Unknown Error"
                  }
               }
               _listFavoriteProgram.value = UiState.Error(errorMessage)
            }
         }

         override fun onFailure(call: Call<AllProgramResponse>, t: Throwable) {
            _listFavoriteProgram.value = UiState.Error(t.message ?: "Unknown error")
         }
      })
   }

   private fun getAllFavoriteSessions() {
      _listFavoriteSession.value = UiState.Loading
      val client = ApiConfig.getApiService().getAllFavoriteSessions("Bearer $token")
      client.enqueue(object : Callback<FavoriteSessionsResponse> {
         override fun onResponse(
            call: Call<FavoriteSessionsResponse>,
            response: Response<FavoriteSessionsResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _listFavoriteSession.value = UiState.Success(resBody.data)
               }

            } else {
               val errorMessage = response.errorBody()?.string().let {
                  if (it != null) {
                     JSONObject(it).getString("message")
                  } else {
                     "Unknown Error"
                  }
               }
               _listFavoriteSession.value = UiState.Error(errorMessage)
            }
         }

         override fun onFailure(call: Call<FavoriteSessionsResponse>, t: Throwable) {
            _listFavoriteSession.value = UiState.Error(t.message ?: "Unknown error")
         }
      })
   }

   fun updateFavoriteSession(id: Int) {
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

                  getAllFavoriteSessions()
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
            }
         }

         override fun onFailure(call: Call<UpdateFavoriteSessionResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   init {
      getAllFavoritePrograms()
      getAllFavoriteSessions()
   }
}
