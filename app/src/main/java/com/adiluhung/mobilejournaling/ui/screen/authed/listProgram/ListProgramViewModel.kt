package com.adiluhung.mobilejournaling.ui.screen.authed.listProgram

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.AllProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.Program
import com.adiluhung.mobilejournaling.ui.common.UiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListProgramViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _uiState = MutableLiveData<UiState<List<Program>>>(UiState.Empty)
   val uiState: LiveData<UiState<List<Program>>>
      get() = _uiState

   private fun getAllPrograms () {
      _uiState.value = UiState.Loading
      val client = ApiConfig.getApiService().getAllPrograms("Bearer $token")
      client.enqueue(object: Callback<AllProgramResponse>{
         override fun onResponse(
            call: Call<AllProgramResponse>,
            response: Response<AllProgramResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _uiState.value = UiState.Success(resBody.data)
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<AllProgramResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   init {
      getAllPrograms()
   }
}