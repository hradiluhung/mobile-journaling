package com.adiluhung.mobilejournaling.data.network.responses

import com.google.gson.annotations.SerializedName

data class AchievementResponse(
   val data: AchievementData
)

data class AchievementData(
   val streak: Int,
   val sessionCount: Int
)

data class UpcomingSessionResponse(
   val data: UpcomingSession
)

data class UpcomingSession(
   val id: Int,
   val type: String,
   val title: String,
   @SerializedName("media_file")
   val mediaFile: String,
   val description: String?,
   @SerializedName("is_favorite")
   val isFavorite: Boolean,
   @SerializedName("module_name")
   val moduleName: String,
   @SerializedName("is_completed")
   val isCompleted: Boolean,
   @SerializedName("media_length")
   val mediaLength: String,
   @SerializedName("program_name")
   val programName: String
)

data class UpdateFavoriteSessionResponse(
   val message : String
)

data class SessionDetailResponse(
   val data: SessionDetail
)

data class FavoriteSessionsResponse(
   val data: List<SessionDetail>
)

data class SessionDetail(
   val id: Int,
   val type: String,
   val title: String,
   @SerializedName("media_file")
   val mediaFile: String,
   val description: String?,
   @SerializedName("is_favorite")
   val isFavorite: Boolean,
   @SerializedName("module_name")
   val moduleName: String,
   @SerializedName("is_completed")
   val isCompleted: Boolean,
   @SerializedName("media_length")
   val mediaLength: String,
   @SerializedName("program_name")
   val programName: String
)

data class CompleteSessionResponse(
   val message: String,
   val data: SessionDetail
)