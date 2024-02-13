package com.adiluhung.mobilejournaling

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class MyApp : Application() {
   override fun onCreate() {
      super.onCreate()

      val notificationChannel = NotificationChannel(
         "journaling_notification",
         "Journaling",
         NotificationManager.IMPORTANCE_HIGH
      )

      val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(notificationChannel)
   }
}