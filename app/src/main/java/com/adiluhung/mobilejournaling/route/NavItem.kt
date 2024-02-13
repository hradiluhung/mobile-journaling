package com.adiluhung.mobilejournaling.route

import com.adiluhung.mobilejournaling.R

// List of Bottom Navbar Item
sealed class NavItem(val route: String, val icon: Int, val activeIcon: Int, val title: String) {
    data object Home: NavItem(Routes.Home.route, R.drawable.ic_home, R.drawable.ic_home_active, "Home")
    data object Mood: NavItem(Routes.ListMood.route, R.drawable.ic_mood, R.drawable.ic_mood_active, "Mood")
    data object Program: NavItem(Routes.ListProgram.route, R.drawable.ic_program, R.drawable.ic_program_active, "Program")
    data object Favorites: NavItem(Routes.Favorites.route, R.drawable.ic_hearth, R.drawable.ic_hearth_active, "Favoties")
    data object Profile: NavItem(Routes.Profile.route, R.drawable.ic_profil, R.drawable.ic_profil_active, "Profile")
}