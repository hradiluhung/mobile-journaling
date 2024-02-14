package com.adiluhung.mobilejournaling

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class JournalingNotificationService(
   private val context: Context
) {
   private val notificationManager = context.getSystemService(NotificationManager::class.java)
   fun showBasicNotification(message: String) {
      // Intent untuk membuka aktivitas utama aplikasi Anda
      val intent = Intent(context, MainActivity::class.java)
      // Menetapkan tindakan untuk intent, jika aplikasi sedang berjalan, intent ini akan menjadi bagian dari tumpukan kegiatan saat ini.
      intent.action = Intent.ACTION_MAIN
      intent.addCategory(Intent.CATEGORY_LAUNCHER)

      // Membuat PendingIntent
      val pendingIntent = PendingIntent.getActivity(
         context,
         0,
         intent,
         PendingIntent.FLAG_IMMUTABLE
      )

      val notification = NotificationCompat.Builder(context, "journaling_notification")
         .setContentTitle("Pengingat Meditasi")
         .setContentText(message)
         .setSmallIcon(R.drawable.ic_mood)
         .setPriority(NotificationManager.IMPORTANCE_HIGH)
         .setAutoCancel(true)
         .setContentIntent(
            pendingIntent
         )
         .build()

      notificationManager.notify(
         Random.nextInt(),
         notification
      )
   }

}