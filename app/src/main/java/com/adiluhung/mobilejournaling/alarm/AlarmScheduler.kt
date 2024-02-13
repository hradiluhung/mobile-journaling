package com.adiluhung.mobilejournaling.alarm

interface AlarmScheduler {
   fun schedule(alarmItem: AlarmItem)
   fun cancel(alarmItem: AlarmItem)
}