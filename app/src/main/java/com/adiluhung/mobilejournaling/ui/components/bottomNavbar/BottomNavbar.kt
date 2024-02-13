package com.adiluhung.mobilejournaling.ui.components.bottomNavbar

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.adiluhung.mobilejournaling.route.NavItem
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun BottomNavbar(navController: NavController) {
   val navItems = listOf(
      NavItem.Home,
      NavItem.Mood,
      NavItem.Program,
      NavItem.Favorites,
      NavItem.Profile,
   )

   val navBackStackEntry by navController.currentBackStackEntryAsState()
   val currentRoute = navBackStackEntry?.destination?.route

   NavigationBar(
      containerColor = Color.White, contentColor = Sky900
   ) {
      navItems.forEach { item ->
         NavigationBarItem(
            selected = currentRoute == item.route,
            onClick = {
               navController.navigate(item.route) {
                  navController.graph.startDestinationRoute?.let { screen_route ->
                     popUpTo(screen_route) {
                        saveState = true
                     }
                  }
                  launchSingleTop = true
                  restoreState = true
               }

            },
            icon = {
               Icon(
                  modifier = Modifier
                     .width(28.dp)
                     .height(28.dp),
                  painter = painterResource(id = item.icon),
                  contentDescription = item.title
               )
            },
            label = null,
         )
      }
   }
}