package com.adiluhung.mobilejournaling.ui.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

val calendar: Calendar = Calendar.getInstance()
val year = calendar.get(Calendar.YEAR)
val month = calendar.get(Calendar.MONTH) + 1
val day = calendar.get(Calendar.DAY_OF_MONTH)
val currentDate: String = LocalDate.of(year, month, day)
   .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

fun getCurrentDateInIndonesian(): String {
   val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
   val currentDate = Date()
   return dateFormat.format(currentDate)
}

fun formatDateTimeToIndonesian(dateTimeString: String): String {
   val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
   val outputFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))

   val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
   return outputFormatter.format(dateTime)
}

fun formatMonthToIndonesian(monthYearString: String): String {
   val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
   val outputFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("id", "ID"))

   val yearMonth = YearMonth.parse(monthYearString, inputFormatter)
   return outputFormatter.format(yearMonth)
}

fun nextMonthAndYear(currentMonthAndYear: String): String {
   val currentYearMonth = YearMonth.parse(currentMonthAndYear, DateTimeFormatter.ofPattern("yyyy-MM"))
   val nextYearMonth = currentYearMonth.plusMonths(1)
   return nextYearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))
}

fun prevMonthAndYear(currentMonthAndYear: String): String {
   val currentYearMonth = YearMonth.parse(currentMonthAndYear, DateTimeFormatter.ofPattern("yyyy-MM"))
   val prevYearMonth = currentYearMonth.minusMonths(1)
   return prevYearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))
}

fun formatDateToIndonesian(dateString: String): String {
   val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
   val outputFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))

   val date = LocalDate.parse(dateString, inputFormatter)
   return outputFormatter.format(date)
}


fun getCurrentMonthInIndonesiaByIndex(index: Int): String {
   val months = arrayOf(
      "Januari",
      "Februari",
      "Maret",
      "April",
      "Mei",
      "Juni",
      "Juli",
      "Agustus",
      "September",
      "Oktober",
      "November",
      "Desember"
   )

   return months[index]
}