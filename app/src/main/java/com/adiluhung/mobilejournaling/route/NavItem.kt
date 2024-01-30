package com.adiluhung.mobilejournaling.route

import com.adiluhung.mobilejournaling.R

// List of Bottom Navbar Item
sealed class NavItem(val route: String, val icon: Int, val activeIcon: Int, val title: String) {
    object Home: NavItem(Routes.Home.route, R.drawable.ic_home, R.drawable.ic_home_active, "Home")
    object Mood: NavItem(Routes.Mood.route, R.drawable.ic_mood, R.drawable.ic_mood_active, "Mood")
    object Program: NavItem(Routes.Program.route, R.drawable.ic_program, R.drawable.ic_program_active, "Program")
    object Profile: NavItem(Routes.Profile.route, R.drawable.ic_profil, R.drawable.ic_profil_active, "Profile")
}