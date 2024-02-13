package com.adiluhung.mobilejournaling.data.network.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
   val data: DataAuth,
   val message: String
)

data class DataAuth(
   val user: User,
   val token: String
)

data class User(
   @field:SerializedName("is_admin")
   val isAdmin: Int,
   @field:SerializedName("updated_at")
   val updatedAt: String,
   @field:SerializedName("last_name")
   val lastName: Any,
   @field:SerializedName("created_at")
   val createdAt: String,
   @field:SerializedName("email_verified_at")
   val emailVerifiedAt: Any,
   val id: Int,
   @field:SerializedName("first_name")
   val firstName: String,
   val email: String
)
