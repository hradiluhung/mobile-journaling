package com.adiluhung.mobilejournaling.data.network.config

import com.adiluhung.mobilejournaling.data.network.responses.AchievementResponse
import com.adiluhung.mobilejournaling.data.network.responses.AllProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.AllTagsResponse
import com.adiluhung.mobilejournaling.data.network.responses.AuthResponse
import com.adiluhung.mobilejournaling.data.network.responses.CheckInMoodResponse
import com.adiluhung.mobilejournaling.data.network.responses.CompleteSessionResponse
import com.adiluhung.mobilejournaling.data.network.responses.CurrentDateMoodResponse
import com.adiluhung.mobilejournaling.data.network.responses.FavoriteSessionsResponse
import com.adiluhung.mobilejournaling.data.network.responses.GetUserProfileResponse
import com.adiluhung.mobilejournaling.data.network.responses.MonthlyMoodResponse
import com.adiluhung.mobilejournaling.data.network.responses.ProgramDetailResponse
import com.adiluhung.mobilejournaling.data.network.responses.RecommendedProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.SessionDetailResponse
import com.adiluhung.mobilejournaling.data.network.responses.TipsResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpcomingSessionResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteProgramResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateFavoriteSessionResponse
import com.adiluhung.mobilejournaling.data.network.responses.UpdateUserProfileResponse
import com.adiluhung.mobilejournaling.data.network.responses.WeeklyMoodResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
   // AUTH
   @FormUrlEncoded
   @POST("register")
   @Headers("Accept: application/json")
   fun register(
      @Field("email") email: String,
      @Field("password") password: String,
      @Field("first_name") firstName: String,
      @Field("last_name") lastName: String?
   ): Call<AuthResponse>

   @FormUrlEncoded
   @Headers("Accept: application/json")
   @POST("login")
   fun login(
      @Field("email") email: String,
      @Field("password") password: String,
   ): Call<AuthResponse>

   // Update Profile
   @FormUrlEncoded
   @Headers("Accept: application/json")
   @POST("profile")
   fun updateProfile(
      @Header("Authorization") token: String,
      @Field("first_name") firstName: String,
      @Field("last_name") lastName: String?,
      @Field("age") age: Int?,
      @Field("gender") gender: String?,
      @Field("school_name") schoolName: String?,
   ): Call<UpdateUserProfileResponse>

   // Get Profile
   @GET("profile")
   fun getUserProfile(
      @Header("Authorization") token: String,
   ): Call<GetUserProfileResponse>

   // Get Weekly Mood
   @GET("moods/{date}/weekly")
   fun getWeeklyMood(
      @Header("Authorization") token: String,
      @Path("date") date: String,
   ): Call<WeeklyMoodResponse>

   // Get Achievements
   @GET("achievements/{date}")
   fun getAchievements(
      @Header("Authorization") token: String,
      @Path("date") date: String,
   ): Call<AchievementResponse>

   // Get Upcoming Sessions
   @GET("sessions/upcoming")
   fun getUpcomingSession(
      @Header("Authorization") token: String,
   ): Call<UpcomingSessionResponse>

   // Get Tips
   @GET("tips")
   fun getTips(
      @Header("Authorization") token: String,
   ): Call<TipsResponse>

   // Get Recommended Programs
   @GET("programs/recommended")
   fun getRecommendedPrograms(
      @Header("Authorization") token: String,
   ): Call<RecommendedProgramResponse>

   // Get Detail Program
   @GET("programs/{id}")
   fun getDetailProgram(
      @Header("Authorization") token: String,
      @Path("id") id: String,
   ): Call<ProgramDetailResponse>

   // Update Favorite Program
   @FormUrlEncoded
   @Headers("Accept: application/json")
   @POST("favorites/programs")
   fun updateFavoriteProgram(
      @Header("Authorization") token: String,
      @Field("program_id") programId: Int,
   ): Call<UpdateFavoriteProgramResponse>

   // Update Favorite Session
   @FormUrlEncoded
   @Headers("Accept: application/json")
   @POST("favorites/sessions")
   fun updateFavoriteSession(
      @Header("Authorization") token: String,
      @Field("session_id") sessionId: Int,
   ): Call<UpdateFavoriteSessionResponse>

   // Get Detail Session
   @GET("sessions/{id}")
   fun getDetailSession(
      @Header("Authorization") token: String,
      @Path("id") id: String,
   ): Call<SessionDetailResponse>

   // Complete Session
   @FormUrlEncoded
   @POST("sessions/{id}/complete")
   @Headers("Accept: application/json")
   fun completeSession(
      @Header("Authorization") token: String,
      @Path("id") id: String,
      @Field("date") date: String,
   ): Call<CompleteSessionResponse>

   // Get All Programs
   @GET("programs")
   fun getAllPrograms(
      @Header("Authorization") token: String,
   ): Call<AllProgramResponse>

   // Get All Tags
   @GET("tags")
   fun getAllTags(
      @Header("Authorization") token: String,
   ): Call<AllTagsResponse>

   // Create CheckIn Mood
   @FormUrlEncoded
   @Headers("Accept: application/json")
   @POST("moods")
   fun createCheckInMood(
      @Header("Authorization") token: String,
      @Field("checkin_date") checkinDate: String,
      @Field("mood") mood: String,
      @Field("note") note: String,
      @Field("tags[]") tags: Array<Int>,
   ): Call<CheckInMoodResponse>

   // Get Mood by Date
   @GET("moods/{date}")
   fun getMoodByDate(
      @Header("Authorization") token: String,
      @Path("date") date: String,
   ): Call<CurrentDateMoodResponse>

   // Get Monthly Mood
   @GET("moods/{date}/monthly")
   fun getMonthlyMood(
      @Header("Authorization") token: String,
      @Path("date") date: String,
   ): Call<MonthlyMoodResponse>

   // Get Favorite Programs
   @GET("favorites/programs")
   fun getAllFavoritePrograms(
      @Header("Authorization") token: String,
   ): Call<AllProgramResponse>

   // Get Favorite Sessions
   @GET("favorites/sessions")
   fun getAllFavoriteSessions(
      @Header("Authorization") token: String,
   ): Call<FavoriteSessionsResponse>

   // Update Email
   @FormUrlEncoded
   @Headers("Accept: application/json")
   @POST("profile/change_email")
   fun updateEmail(
      @Header("Authorization") token: String,
      @Field("email") email: String,
   ): Call<UpdateUserProfileResponse>

   /// Update Password
   @FormUrlEncoded
   @Headers("Accept: application/json")
   @POST("profile/change_password")
   fun updatePassword(
      @Header("Authorization") token: String,
      @Field("password") password: String,
      @Field("password_confirmation") passwordConfirmation: String,
   ): Call<UpdateUserProfileResponse>

   // Update Profile Picture
   @Multipart
   @POST("profile/photo")
   fun updateProfilePicture(
      @Header("Authorization") token: String,
      @Part photo: MultipartBody.Part,
   ): Call<UpdateUserProfileResponse>
}