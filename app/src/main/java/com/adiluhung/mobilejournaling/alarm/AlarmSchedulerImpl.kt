package com.adiluhung.mobilejournaling.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.adiluhung.mobilejournaling.AlarmReceiver
import java.time.LocalDate

class AlarmSchedulerImpl(
   private val context: Context
) : AlarmScheduler {

   private val alarmManager = context.getSystemService(AlarmManager::class.java)

   override fun schedule(alarmItem: AlarmItem) {
      val intent = Intent(context, AlarmReceiver::class.java).apply {
         putExtra("EXTRA_MESSAGE", alarmItem.message)
      }
      val alarmTime = alarmItem.alarmTime.timeInMillis
      alarmManager.setInexactRepeating(
         AlarmManager.RTC_WAKEUP,
         alarmTime,
         AlarmManager.INTERVAL_DAY,
         PendingIntent.getBroadcast(
            context,
            alarmItem.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
         )
      )
   }

   override fun cancel(alarmItem: AlarmItem) {
      alarmManager.cancel(
         PendingIntent.getBroadcast(
            context,
            alarmItem.hashCode(),
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
         )
      )
   }


}