package com.adiluhung.mobilejournaling.ui.utils

val greeting = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
   in 0..11 -> "Selamat Pagi!"
   in 12..15 -> "Selamat Siang!"
   in 16..18 -> "Selamat Sore!"
   in 19..23 -> "Selamat Malam!"
   else -> "Selamat Datang!"
}