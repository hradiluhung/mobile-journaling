package com.adiluhung.mobilejournaling.data.network.responses

import com.google.gson.annotations.SerializedName

data class WeeklyMoodResponse(
   val data: Map<String, Mood>
)

data class MonthlyMoodResponse(
   val data: MonthlyMood
)

data class MonthlyMood(
   @SerializedName("first_day")
   val firstDay: String,
   val days: Int,
   @SerializedName("month_name")
   val monthName: String,
   val moods: List<Mood>
)

data class CurrentDateMoodResponse(
   val data: Mood
)

data class Mood(
   val id: Int,
   val day: String,
   val mood: String,
   val note: String,
   val tags: List<Tag>,
   @SerializedName("user_id")
   val userId: Int,
   @SerializedName("checkin_date")
   val checkinDate: String
)

data class Tag(
   val id: Int,
   val name: String
)

data class AllTagsResponse(
   val data: List<Tag>
)

data class CheckInMoodResponse(
   val message: String
)
