package com.adiluhung.mobilejournaling.alarm

import java.util.Calendar

data class AlarmItem(
   val alarmTime : Calendar,
   val message : String
)