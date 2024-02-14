package com.adiluhung.mobilejournaling

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {
   @SuppressLint("MissingPermission")
   override fun onReceive(context: Context?, intent: Intent?) {
      // val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
      //
      // context?.let {
      //    val notificationService = JournalingNotificationService(context)
      //    notificationService.showBasicNotification(message)
      // }

      val i = Intent(context, MainActivity::class.java)
      intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)

      val builder = NotificationCompat.Builder(context!!, "journaling_notification")
         .setSmallIcon(R.drawable.ic_launcher_foreground)
         .setContentTitle("Pengingat Meditasi")
         .setContentText("Waktunya meditasi")
         .setAutoCancel(true)
         .setDefaults(NotificationCompat.DEFAULT_ALL)
         .setPriority(NotificationCompat.PRIORITY_HIGH)
         .setContentIntent(pendingIntent)

      val notificationManager = NotificationManagerCompat.from(context)

      notificationManager.notify(123, builder.build())
   }

}