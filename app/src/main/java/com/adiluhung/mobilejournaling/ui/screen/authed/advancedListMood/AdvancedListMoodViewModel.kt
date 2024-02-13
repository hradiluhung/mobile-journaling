package com.adiluhung.mobilejournaling.ui.screen.authed.advancedListMood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.MonthlyMoodResponse
import com.adiluhung.mobilejournaling.data.network.responses.Mood
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.utils.currentDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvancedListMoodViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _currentMonthAndYear =
      MutableLiveData(currentDate.split("-")[0] + "-" + currentDate.split("-")[1])
   val currentMonthAndYear: LiveData<String>
      get() = _currentMonthAndYear

   private val _monthlyMood = MutableLiveData<UiState<List<Mood>>>(UiState.Empty)
   val monthlyMood: LiveData<UiState<List<Mood>>>
      get() = _monthlyMood

   private fun getMonthlyMood() {
      _monthlyMood.value = UiState.Loading
      val client =
         ApiConfig.getApiService()
            .getMonthlyMood("Bearer $token", "${currentMonthAndYear.value}-01")
      client.enqueue(object : Callback<MonthlyMoodResponse> {
         override fun onResponse(
            call: Call<MonthlyMoodResponse>,
            response: Response<MonthlyMoodResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _monthlyMood.value = UiState.Success(resBody.data.moods)
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
               _monthlyMood.value = UiState.Error(errorMessage)
            }
         }

         override fun onFailure(call: Call<MonthlyMoodResponse>, t: Throwable) {
            _monthlyMood.value = UiState.Error(t.message ?: "Unknown error")
         }
      })
   }

   fun updateCurrentMonthAndYear(monthAndYear: String) {
      val isDifferentMonth = monthAndYear != currentMonthAndYear.value
      _currentMonthAndYear.value = monthAndYear

      if (isDifferentMonth) {
         getMonthlyMood()
      }
   }

   init {
      getMonthlyMood()
   }
}
