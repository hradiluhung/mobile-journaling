package com.adiluhung.mobilejournaling.ui.screen.authed.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.AchievementData
import com.adiluhung.mobilejournaling.data.network.responses.AchievementResponse
import com.adiluhung.mobilejournaling.data.network.responses.CurrentDateMoodResponse
import com.adiluhung.mobilejournaling.data.network.responses.Mood
import com.adiluhung.mobilejournaling.data.network.responses.GetUserProfileResponse
import com.adiluhung.mobilejournaling.data.network.responses.RecommendedProgram
import com.adiluhung.mobilejournaling.data.network.responses.RecommendedProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.TipsResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpcomingSession
import com.adiluhung.mobilejournaling.data.network.responses.UpcomingSessionResponse
import com.adiluhung.mobilejournaling.data.network.responses.WeeklyMoodResponse
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.utils.currentDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   data class HomeData(
      val userFullName: String = "",
      val weeklyMood: WeeklyMoodResponse? = null,
      val achievements: AchievementData? = null,
      val upcomingSession: UpcomingSession? = null,
      val tips: String = "",
      val recommendedProgram : List<RecommendedProgram>,
      val isCheckedIn : Boolean
   )

   private val _uiState = MutableLiveData<UiState<HomeData>>(UiState.Empty)
   val uiState: LiveData<UiState<HomeData>>
      get() = _uiState


   private val _userFullName = MutableLiveData<String>()
   private val _weeklyMood = MutableLiveData<WeeklyMoodResponse?>()
   private val _achievements = MutableLiveData<AchievementData?>()
   private val _upcomingSession = MutableLiveData<UpcomingSession?>()
   private val _tips = MutableLiveData<String>()
   private val _recommendedProgram = MutableLiveData<List<RecommendedProgram>>()
   private val _isCheckedIn = MutableLiveData<Boolean>()


   private fun getUserFullName() {
      val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
      client.enqueue(object : Callback<GetUserProfileResponse> {
         override fun onResponse(
            call: Call<GetUserProfileResponse>,
            response: Response<GetUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _userFullName.value = resBody.data.firstName + " " + resBody.data.lastName
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   private fun getWeeklyMood() {
      val client = ApiConfig.getApiService().getWeeklyMood("Bearer $token", currentDate)
      client.enqueue(object : Callback<WeeklyMoodResponse> {
         override fun onResponse(
            call: Call<WeeklyMoodResponse>,
            response: Response<WeeklyMoodResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _weeklyMood.value = resBody
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<WeeklyMoodResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   private fun getAchievements() {
      val client = ApiConfig.getApiService().getAchievements("Bearer $token", currentDate)
      client.enqueue(object : Callback<AchievementResponse> {
         override fun onResponse(
            call: Call<AchievementResponse>,
            response: Response<AchievementResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _achievements.value = resBody.data
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<AchievementResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   private fun getUpComingSessions() {
      val client = ApiConfig.getApiService().getUpcomingSession("Bearer $token")
      client.enqueue(object : Callback<UpcomingSessionResponse> {
         override fun onResponse(
            call: Call<UpcomingSessionResponse>,
            response: Response<UpcomingSessionResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _upcomingSession.value = resBody.data
               }
            } else {
               _upcomingSession.value = null
            }
         }

         override fun onFailure(call: Call<UpcomingSessionResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   private fun getTips() {
      val client = ApiConfig.getApiService().getTips("Bearer $token")
      client.enqueue(object : Callback<TipsResponse> {
         override fun onResponse(
            call: Call<TipsResponse>,
            response: Response<TipsResponse>
         ) {
            if (response.isSuccessful) {
               val data = response.body()
               if (data != null) {
                  _tips.value = data.data.quote
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<TipsResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
         }
      })
   }

   private fun getRecommendedProgram() {
      val client = ApiConfig.getApiService().getRecommendedPrograms("Bearer $token")
      client.enqueue(object : Callback<RecommendedProgramResponse> {
         override fun onResponse(
            call: Call<RecommendedProgramResponse>,
            response: Response<RecommendedProgramResponse>
         ) {
            if (response.isSuccessful) {
               val data = response.body()
               if (data != null) {
                  _recommendedProgram.value = data.data
               }
            } else {
               Log.e("HomeViewModel", "Error: ${response.code()}")
            }
         }

         override fun onFailure(call: Call<RecommendedProgramResponse>, t: Throwable) {
            Log.e("HomeViewModel", "Error: ${t.message}")
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


   private fun updateUiState() {
      val userFullName = _userFullName.value ?: ""
      val weeklyMood = _weeklyMood.value
      val achievements = _achievements.value
      val upcomingSession = _upcomingSession.value
      val tips = _tips.value ?: ""
      val recommendedProgram = _recommendedProgram.value ?: emptyList()
      val isCheckedIn = _isCheckedIn.value ?: false

      _uiState.value = UiState.Success(
         HomeData(
            userFullName = userFullName,
            weeklyMood = weeklyMood,
            achievements = achievements,
            upcomingSession = upcomingSession,
            tips = tips,
            recommendedProgram = recommendedProgram,
            isCheckedIn = isCheckedIn
         )
      )
   }

   fun getAllData() {
      _uiState.value = UiState.Loading
      getUserFullName()
      getWeeklyMood()
      getAchievements()
      getUpComingSessions()
      getTips()
      getRecommendedProgram()
      isCheckedIn()
   }

   init {
      _userFullName.observeForever {
         updateUiState()
      }
      _weeklyMood.observeForever {
         updateUiState()
      }
      _achievements.observeForever {
         updateUiState()
      }
      _upcomingSession.observeForever {
         updateUiState()
      }
      _tips.observeForever {
         updateUiState()
      }
      _recommendedProgram.observeForever {
         updateUiState()
      }
      _isCheckedIn.observeForever {
         updateUiState()
      }
   }
}