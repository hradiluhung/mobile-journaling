package com.adiluhung.mobilejournaling

import android.app.NotificationChannel
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.adiluhung.mobilejournaling.ui.JournalingApp
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme

class MainActivity : ComponentActivity() {
   @RequiresApi(Build.VERSION_CODES.TIRAMISU)
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      // Make status bar transparent
      // window.setFlags(
      //     WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
      //     WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
      // )

      setContent {
         JournalingTheme(darkTheme = false) {
            // A surface container using the 'background' color from the theme
            Surface(
               modifier = Modifier.fillMaxSize(),
               color = MaterialTheme.colorScheme.background
            ) {
               JournalingApp()
            }
         }
      }
   }
}