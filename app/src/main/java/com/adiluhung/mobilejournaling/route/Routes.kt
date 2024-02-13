package com.adiluhung.mobilejournaling.route

sealed class Routes(val route: String) {
   // common
   data object Loading : Routes("loading")
   data object CompleteProfile : Routes("completeProfile")

   // unauthenticated
   data object Login : Routes("login")
   data object Register : Routes("register")
   data object Start : Routes("start")

   // authenticated
   data object Home : Routes("home")
   data object ListMood : Routes("listMood")
   data object ListProgram : Routes("listProgram")
   data object Favorites : Routes("Favorites")
   data object Profile : Routes("profile")

   data object DetailProgram : Routes("detailProgram/{programId}") {
      fun createRoute(programId: Int) = "detailProgram/$programId"
   }

   data object DetailSession : Routes("detailSession/{sessionId}") {
      fun createRoute(sessionId: Int) = "detailSession/$sessionId"
   }

   data object MoodCheckIn : Routes("moodCheckIn")


   data object MoodAddNote : Routes("moodAddNote/{moodId}") {
      fun createRoute(moodId: Int) = "moodAddNote/$moodId"
   }

   data object AdvancedListMood : Routes("advancedListMood")

   data object PersonalInfo : Routes("personalInfo")

   data object AccountInfo : Routes("accountInfo")

   data object Reminder : Routes("reminder")

   data object SessionComplete : Routes("sessionComplete/{sessionId}") {
      fun createRoute(sessionId: Int) = "sessionComplete/$sessionId"
   }
}