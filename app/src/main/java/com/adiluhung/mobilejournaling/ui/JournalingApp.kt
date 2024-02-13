package com.adiluhung.mobilejournaling.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.screen.authed.accountInfo.AccountInfoScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.advancedListMood.AdvancedListMoodScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.completeProfile.CompleteProfileScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.detailProgram.DetailProgramScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.detailSession.DetailSessionScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.home.HomeScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.listFavoriteProgram.ListFavoriteScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.listMood.ListMoodScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.listProgram.ListProgramScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.moodAddNote.MoodAddNoteScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.moodCheckIn.MoodCheckInScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.personalInfo.PersonalInfoScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.profile.ProfileScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.reminder.ReminderScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.sessionComplete.SessionCompleteScreen
import com.adiluhung.mobilejournaling.ui.screen.common.LoadingScreen
import com.adiluhung.mobilejournaling.ui.screen.guest.LoginScreen
import com.adiluhung.mobilejournaling.ui.screen.guest.RegisterScreen
import com.adiluhung.mobilejournaling.ui.screen.guest.StartScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun JournalingApp(
   viewModel: MainViewModel = viewModel(
      factory = ViewModelFactory(context = LocalContext.current)
   )
) {
   val navController = rememberNavController()
   val token = viewModel.token.observeAsState().value
   var startDestination by remember { mutableStateOf(Routes.Loading.route) }

   val isProfileCompleted = viewModel.isProfileComplete.observeAsState().value

   viewModel.uiState.observeAsState(initial = UiState.Empty).value.let { uiState ->
      SideEffect {
         when (uiState) {
            is UiState.Loading -> {
               startDestination = Routes.Loading.route
            }

            is UiState.Error -> {
               Log.d("JournalingApp", "Error: ${uiState.errorMessage}")
            }

            is UiState.Success -> {
               viewModel.observeCompleteProfile()

               startDestination = if (token == null) {
                  Routes.Start.route
               } else {
                  if (isProfileCompleted == false) {
                     Routes.CompleteProfile.route
                  } else {
                     Routes.Home.route
                  }
               }
            }

            is UiState.Empty -> {
               startDestination = Routes.Loading.route
            }
         }
      }
   }

   NavHost(navController, startDestination = startDestination) {

      composable(Routes.Start.route) {
         StartScreen(navController)
      }

      composable(
         route = Routes.Register.route,
         enterTransition = {
            slideInHorizontally(
               initialOffsetX = { fullWidth -> fullWidth },
               animationSpec = tween(durationMillis = 500)
            )
         },
         exitTransition = {
            slideOutHorizontally(
               targetOffsetX = { fullWidth -> fullWidth },
               animationSpec = tween(durationMillis = 500)
            )
         }
      ) {
         RegisterScreen(navController)
      }

      composable(route = Routes.Loading.route,
         enterTransition = {
            slideInHorizontally(
               initialOffsetX = { fullWidth -> fullWidth },
               animationSpec = tween(durationMillis = 500)
            )
         },
         exitTransition = {
            slideOutHorizontally(
               targetOffsetX = { fullWidth -> fullWidth },
               animationSpec = tween(durationMillis = 500)
            )
         }
      ) {
         LoadingScreen()
      }

      composable(Routes.Login.route) {
         LoginScreen(navController)
      }

      composable(Routes.Home.route) {
         HomeScreen(navController)
      }

      composable(Routes.ListMood.route) {
         ListMoodScreen(navController = navController)
      }

      composable(Routes.Profile.route) {
         ProfileScreen(navController = navController)
      }

      composable(Routes.ListProgram.route) {
         ListProgramScreen(navController = navController)
      }

      composable(Routes.DetailProgram.route) {
         DetailProgramScreen(navController, it.arguments?.getString("programId") ?: "0")
      }

      composable(Routes.DetailSession.route) {
         DetailSessionScreen(
            navController,
            it.arguments?.getString("sessionId") ?: "0"
         )
      }

      composable(Routes.CompleteProfile.route) {
         CompleteProfileScreen(navController = navController)
      }

      composable(Routes.MoodCheckIn.route) {
         MoodCheckInScreen(navController = navController)
      }

      composable(Routes.MoodAddNote.route) {
         MoodAddNoteScreen(
            navController,
            it.arguments?.getString("moodId") ?: "0"
         )
      }

      composable(Routes.AdvancedListMood.route) {
         AdvancedListMoodScreen(navController = navController)
      }

      composable(Routes.Favorites.route) {
         ListFavoriteScreen(navController = navController)
      }

      composable(Routes.PersonalInfo.route) {
         PersonalInfoScreen(navController = navController)
      }

      composable(Routes.AccountInfo.route) {
         AccountInfoScreen(navController = navController)
      }

      composable(Routes.Reminder.route) {
         ReminderScreen(navController = navController)
      }

      composable(Routes.SessionComplete.route){
         SessionCompleteScreen(
            navController = navController,
            sessionId = it.arguments?.getString("sessionId") ?: "0"
         )
      }
   }
}