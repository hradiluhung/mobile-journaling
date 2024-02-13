package com.adiluhung.mobilejournaling.data.network.responses

import com.google.gson.annotations.SerializedName

data class RecommendedProgramResponse(
   val data: List<RecommendedProgram>
)

data class RecommendedProgram(
   val id: Int,
   val image: String?,
   val title: String,
   val progress: Int,
   val description: String,
   @SerializedName("modules_count")
   val modulesCount: Int,
   @SerializedName("sessions_count")
   val sessionsCount: Int
)

data class ProgramDetailResponse(
   val data: ProgramDetail
)

data class ProgramDetail(
   val id: Int,
   val image: String?,
   val title: String,
   val modules: List<Module>,
   val description: String,
   @SerializedName("is_favorite")
   var isFavorite: Boolean,
)

data class Module(
   val id: Int,
   val title: String,
   val sessions: List<Session>,
   @SerializedName("total_duration")
   val totalDuration: String
)

data class Session(
   val id: Int,
   val type: String?,
   val title: String,
   @SerializedName("media_file")
   val mediaFile: String,
   val description: String?,
   @SerializedName("is_favorite")
   var isFavorite: Boolean,
   @SerializedName("module_name")
   val moduleName: String,
   @SerializedName("is_completed")
   val isCompleted: Boolean,
   @SerializedName("media_length")
   val mediaLength: String,
   @SerializedName("program_name")
   val programName: String
)

data class UpdateFavoriteProgramResponse(
   val message : String
)

data class AllProgramResponse(
   val data: List<Program>
)

data class Program(
   val id: Int,
   val image: String?,
   val title: String,
   val progress: Int,
   val description: String,
   @SerializedName("modules_count")
   val modulesCount: Int,
   @SerializedName("sessions_count")
   val sessionsCount: Int
)
