package com.adiluhung.mobilejournaling.data.network.responses
import com.google.gson.annotations.SerializedName

data class TipsResponse(
   val data: TipsData
)

data class TipsData(
   val id: Int,
   val quote: String,
   @SerializedName("created_at")
   val createdAt: String,
   @SerializedName("updated_at")
   val updatedAt: String
)
