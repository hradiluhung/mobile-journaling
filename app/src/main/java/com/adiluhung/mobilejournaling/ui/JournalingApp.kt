package com.adiluhung.mobilejournaling.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.screen.authed.detailProgram.DetailProgramScreen
import com.adiluhung.mobilejournaling.ui.screen.authed.home.HomeScreen
import com.adiluhung.mobilejournaling.ui.screen.guest.LoginScreen
import com.adiluhung.mobilejournaling.ui.screen.guest.RegisterScreen
import com.adiluhung.mobilejournaling.ui.screen.guest.StartScreen

@Composable
fun JournalingApp(
    viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    )
) {
    val navController = rememberNavController()
    val token = viewModel.getLoggedInUser().observeAsState().value

    val startDestination = if (token == null) {
        Routes.Start.route
    } else {
        Routes.Home.route
    }

    NavHost(navController, startDestination = startDestination){
        composable(Routes.Start.route){
            StartScreen(navController)
        }

        composable(Routes.Register.route){
            RegisterScreen(navController)
        }

        composable(Routes.Login.route){
            LoginScreen(navController)
        }

        composable(Routes.Home.route){
            HomeScreen(navController)
        }

        composable(Routes.DetailProgram.route) {
            DetailProgramScreen(navController, it.arguments?.getString("id")?.toInt() ?: 0)
        }
    }
}