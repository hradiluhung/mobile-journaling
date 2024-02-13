package com.adiluhung.mobilejournaling

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
   override fun onReceive(context: Context?, intent: Intent?) {
      val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return

      context?.let { ctx ->
         val notificationService = JournalingNotificationService(context)
         notificationService.showBasicNotification(message)
      }
   }
}