package com.adiluhung.mobilejournaling.data.network.responses

import com.google.gson.annotations.SerializedName

data class UpdateUserProfileResponse(
   val data: DataProfile,
   val message: String
)

data class GetUserProfileResponse(
   val data: DataProfile
)

data class DataProfile(
   val gender: String? = null,
   @field:SerializedName("updated_at")
   val updatedAt: String,
   @field:SerializedName("last_name")
   val lastName: String? = null,
   @field:SerializedName("created_at")
   val createdAt: String,
   @field:SerializedName("school_name")
   val schoolName: String? = null,
   val id: Int,
   @field:SerializedName("user_id")
   val photoUrl: String? = null,
   @field:SerializedName("first_name")
   val firstName: String,
   val age: Int? = null,
   val email: String
)
