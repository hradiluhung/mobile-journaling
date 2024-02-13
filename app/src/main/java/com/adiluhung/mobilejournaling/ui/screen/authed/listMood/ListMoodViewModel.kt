package com.adiluhung.mobilejournaling.ui.screen.authed.listMood

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.CurrentDateMoodResponse
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

class ListMoodViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _monthlyMood = MutableLiveData<UiState<List<Mood>>>(UiState.Empty)
   val monthlyMood: LiveData<UiState<List<Mood>>>
      get() = _monthlyMood

   private val _selectedMood = MutableLiveData<UiState<Mood?>>(UiState.Empty)
   val selectedMood: LiveData<UiState<Mood?>>
      get() = _selectedMood

   private val _currentDateState = MutableLiveData(currentDate)
   private val currentDateState: LiveData<String>
      get() = _currentDateState

   private val _currentMonthAndYear =
      MutableLiveData(currentDate.split("-")[0] + "-" + currentDate.split("-")[1])
   private val currentMonthAndYear: LiveData<String>
      get() = _currentMonthAndYear

   private val _isCheckedIn = MutableLiveData<Boolean>()
   val isCheckedIn: LiveData<Boolean>
      get() = _isCheckedIn


   fun updateCurrentDate(date: String) {
      val isDifferentDate = date != currentDateState.value
      _currentDateState.value = date

      if (isDifferentDate) {
         getMoodByDate()
      }
   }

   fun updateCurrentMonthAndYear(monthAndYear: String) {
      val isDifferentMonth = monthAndYear != currentMonthAndYear.value
      _currentMonthAndYear.value = monthAndYear

      if (isDifferentMonth) {
         getMonthlyMood()
      }
   }

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

   private fun getMoodByDate() {
      _selectedMood.value = UiState.Loading
      val client = ApiConfig.getApiService()
         .getMoodByDate("Bearer $token", currentDateState.value!!)
      client.enqueue(object : Callback<CurrentDateMoodResponse> {
         override fun onResponse(
            call: Call<CurrentDateMoodResponse>,
            response: Response<CurrentDateMoodResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _selectedMood.value = UiState.Success(resBody.data)
               }
            } else {
               _selectedMood.value = UiState.Success(null)
            }
         }

         override fun onFailure(call: Call<CurrentDateMoodResponse>, t: Throwable) {
            _selectedMood.value = UiState.Error(t.message ?: "Unknown error")
            Log.e("ListMoodViewModel", t.message ?: "Unknown error")
         }
      })
   }

   private fun isCheckedIn() {
      val client = ApiConfig.getApiService().getMoodByDate("Bearer $token", currentDate)
      client.enqueue(object : Callback<CurrentDateMoodResponse> {
         override fun onResponse(
            call: Call<CurrentDateMoodResponse>,
            response: Response<CurrentDateMoodResponse>
         ) {
            _isCheckedIn.value = response.isSuccessful
         }

         override fun onFailure(call: Call<CurrentDateMoodResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   init {
      getMonthlyMood()
      getMoodByDate()
      isCheckedIn()
   }
}