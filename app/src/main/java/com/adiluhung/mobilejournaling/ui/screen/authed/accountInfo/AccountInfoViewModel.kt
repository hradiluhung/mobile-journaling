package com.adiluhung.mobilejournaling.ui.screen.authed.accountInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiluhung.mobilejournaling.data.local.UserPreferences
import com.adiluhung.mobilejournaling.data.network.config.ApiConfig
import com.adiluhung.mobilejournaling.data.network.responses.DataProfile
import com.adiluhung.mobilejournaling.data.network.responses.GetUserProfileResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateUserProfileResponse
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

class AccountInfoViewModel(private val pref: UserPreferences) : ViewModel() {
   private val token = runBlocking {
      pref.getUserToken()
         .map { token -> token }
         .first()
   }

   private val _profileData = MutableLiveData<DataProfile>()
   val profileData: LiveData<DataProfile>
      get() = _profileData

   private val _isLoadingInit = MutableLiveData<Boolean>()
   val isLoadingInit: LiveData<Boolean>
      get() = _isLoadingInit

   private val _updateState = MutableLiveData<UiState<Boolean>>(UiState.Empty)
   val updateState: MutableLiveData<UiState<Boolean>>
      get() = _updateState

   private val _toastMessage = MutableSharedFlow<String>()
   val toastMessage = _toastMessage.asSharedFlow()

   fun sendMessage(message: String) {
      viewModelScope.launch {
         _toastMessage.emit(message)
      }
   }

   private fun getProfileUser() {
      _isLoadingInit.value = true
      val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
      client.enqueue(object : Callback<GetUserProfileResponse> {
         override fun onResponse(
            call: Call<GetUserProfileResponse>,
            response: Response<GetUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _profileData.value = resBody.data
               }
            } else {
               _isLoadingInit.value = false
               val errorMessage = response.errorBody()?.string().let {
                  if (it != null) {
                     JSONObject(it).getString("message")
                  } else {
                     "Unknown Error"
                  }
               }
               sendMessage(errorMessage)
            }
         }

         override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
            sendMessage(t.message.toString())
         }
      })
   }

   private fun updateEmail(email: String) {
      _updateState.value = UiState.Loading
      val client = ApiConfig.getApiService().updateEmail("Bearer $token", email)
      client.enqueue(object : Callback<UpdateUserProfileResponse> {
         override fun onResponse(
            call: Call<UpdateUserProfileResponse>,
            response: Response<UpdateUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _updateState.value = UiState.Success(true)
                  sendMessage(resBody.message)
               }
            } else {
               val errorMessage = response.errorBody()?.string().let {
                  if (it != null) {
                     JSONObject(it).getString("message")
                  } else {
                     "Unknown Error"
                  }
               }
               _updateState.value = UiState.Error(errorMessage)
               sendMessage(errorMessage)
            }
         }

         override fun onFailure(call: Call<UpdateUserProfileResponse>, t: Throwable) {
            _updateState.value = UiState.Error(t.message.toString())
            sendMessage(t.message.toString())
         }
      })
   }

   private fun updatePassword(password: String, passwordConfirmation: String) {
      _updateState.value = UiState.Loading
      val client =
         ApiConfig.getApiService().updatePassword("Bearer $token", password, passwordConfirmation)
      client.enqueue(object : Callback<UpdateUserProfileResponse> {
         override fun onResponse(
            call: Call<UpdateUserProfileResponse>,
            response: Response<UpdateUserProfileResponse>
         ) {
            if (response.isSuccessful) {
               val resBody = response.body()
               if (resBody != null) {
                  _updateState.value = UiState.Success(true)
                  sendMessage(resBody.message)
               }
            } else {
               val errorMessage = response.errorBody()?.string().let {
                  if (it != null) {
                     JSONObject(it).getString("message")
                  } else {
                     "Unknown Error"
                  }
               }
               _updateState.value = UiState.Error(errorMessage)
               sendMessage(errorMessage)
            }
         }

         override fun onFailure(call: Call<UpdateUserProfileResponse>, t: Throwable) {
            _updateState.value = UiState.Error(t.message.toString())
            sendMessage(t.message.toString())
         }
      })
   }

   fun updateEmailAndPassword(email: String, password: String, passwordConfirmation: String) {
      if (password != "" && passwordConfirmation != "") {
         updatePassword(password, passwordConfirmation)
      }
      updateEmail(email)
   }


   init {
      getProfileUser()
   }

}
