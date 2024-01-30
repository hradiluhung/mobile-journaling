package com.adiluhung.mobilejournaling.route

sealed class Routes(val route: String) {
    // unauthenticated
    object Login : Routes("login")
    object Register : Routes("register")
    object Start : Routes("start")

    // authenticated
    object Home : Routes("home")
    object Mood : Routes("mood")
    object Program: Routes("program")
    object Profile : Routes("profile")

    object DetailProgram: Routes("detailProgram/{id}") {
        fun createRoute(id: Int) = "detailProgram/$id"
    }
}