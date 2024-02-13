package com.adiluhung.mobilejournaling.ui.components.calendar

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adiluhung.mobilejournaling.data.network.responses.Mood
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.Sky300
import com.adiluhung.mobilejournaling.ui.theme.Sky50
import com.adiluhung.mobilejournaling.ui.utils.currentDate
import com.adiluhung.mobilejournaling.ui.utils.getCurrentMonthInIndonesiaByIndex
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class CalendarDate(val date: LocalDate, val isInCurrentMonth: Boolean)

@Composable
fun Calendar(
   modifier: Modifier = Modifier,
   selectedDate: LocalDate,
   listMood: List<Mood>,
   onChangeSelectedDate : (LocalDate) -> Unit,
   onChangeMonthAndYear: (Int, Int) -> Unit
) {
   var currentMonth by remember { mutableIntStateOf(currentDate.split("-")[1].toInt()) }
   val currentMonthText = getCurrentMonthInIndonesiaByIndex(currentMonth - 1)
   var currentYear by remember { mutableIntStateOf(currentDate.split("-")[0].toInt()) }

   var daysInMonth by remember {
      mutableIntStateOf(
         YearMonth.of(currentYear, currentMonth).lengthOfMonth()
      )
   }
   val firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1).dayOfWeek.value % 7
   val context = LocalContext.current

   var days by remember { mutableStateOf<List<CalendarDate>>(mutableStateListOf()) }
   var rowIndex = 0

   fun updateDays() {
      days = emptyList()
      rowIndex = 0

      // Add last days of the previous month
      val lastDayOfPreviousMonth =
         YearMonth.of(currentYear, currentMonth).minusMonths(1).lengthOfMonth()
      val firstDayOfWeek = LocalDate.of(currentYear, currentMonth, 1).dayOfWeek.value % 7
      val startDay = lastDayOfPreviousMonth - firstDayOfWeek + 1
      for (i in startDay..lastDayOfPreviousMonth) {
         val newValue = CalendarDate(
            LocalDate.of(
               currentYear,
               if (currentMonth == 1) 12 else currentMonth - 1,
               i
            ), false
         )
         days = days + newValue
         rowIndex++
      }

      daysInMonth = YearMonth.of(currentYear, currentMonth).lengthOfMonth()

      // Add days of the current month
      for (i in 1..daysInMonth) {
         val newValue = CalendarDate(
            LocalDate.of(currentYear, currentMonth, i),
            true
         )
         days = days + newValue
         rowIndex++
      }

      // Add first days of the next month
      val remainingDays = 7 - (rowIndex % 7)
      for (i in 1..remainingDays) {
         val newValue = CalendarDate(
            LocalDate.of(
               currentYear,
               if (currentMonth == 12) 1 else currentMonth + 1,
               i
            ), false
         )
         days = days + newValue
      }
   }

   LaunchedEffect(Unit) {
      // Add last days of the previous month
      val lastDayOfPreviousMonth =
         YearMonth.of(currentYear, currentMonth).minusMonths(1).lengthOfMonth()
      val firstDayOfWeek = LocalDate.of(currentYear, currentMonth, 1).dayOfWeek.value % 7
      val startDay = lastDayOfPreviousMonth - firstDayOfWeek + 1
      for (i in startDay..lastDayOfPreviousMonth) {
         val newValue = CalendarDate(
            LocalDate.of(
               currentYear,
               if (currentMonth == 1) 12 else currentMonth - 1,
               i
            ), false
         )
         days = days + newValue
         rowIndex++
      }

      // Add days of the current month
      for (i in 1..daysInMonth) {
         val newValue = CalendarDate(
            LocalDate.of(currentYear, currentMonth, i),
            true
         )
         days = days + newValue
         rowIndex++
      }

      // Add first days of the next month
      val remainingDays = 7 - (rowIndex % 7)
      for (i in 1..remainingDays) {
         val newValue = CalendarDate(
            LocalDate.of(
               currentYear,
               if (currentMonth == 12) 1 else currentMonth + 1,
               i
            ), false
         )
         days = days + newValue
      }
   }

   fun previousMonth() {
      if (currentMonth == 1) {
         currentMonth = 12
         currentYear--
      } else {
         currentMonth--
      }

      updateDays()
      onChangeMonthAndYear(currentMonth, currentYear)
   }

   fun nextMonth() {
      if (currentMonth == 12) {
         currentMonth = 1
         currentYear++
      } else {
         currentMonth++
      }

      updateDays()
      onChangeMonthAndYear(currentMonth, currentYear)
   }

   Card(
      modifier = modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
         containerColor = Color.White,
      ),
      elevation = CardDefaults.cardElevation(2.dp),
      border = BorderStroke(1.dp, Color.LightGray)
   ) {
      Column(
         modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
      ) {
         Row(
            modifier = Modifier
               .fillMaxWidth()
               .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
         ) {

            Text(
               text = "$currentMonthText $currentYear",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  lineHeight = 24.sp
               ),
               textAlign = TextAlign.Center,
               modifier = Modifier.padding(horizontal = 16.dp)
            )
            Row {
               IconButton(onClick = { previousMonth() }) {
                  Icon(
                     Icons.Default.KeyboardArrowLeft,
                     contentDescription = "Previous Month",
                  )
               }

               IconButton(onClick = { nextMonth() }) {
                  Icon(
                     Icons.Default.KeyboardArrowRight,
                     contentDescription = "Next Month",
                  )
               }

            }
         }

         RowWithDaysOfWeek()

         Spacer(modifier = Modifier.height(8.dp))

         Column(
            modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp)
         ) {
            days.chunked(7).forEach { week ->
               Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
               ) {
                  week.forEach { day ->
                     val mood = listMood.find { mood ->
                        val formatter =
                           DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
                        val dateTime = LocalDateTime.parse(mood.checkinDate, formatter)
                        val date = dateTime.toLocalDate()

                        date == day.date
                     }

                     Day(
                        modifier = Modifier
                           .weight(1f),
                        date = day,
                        isSelected = day.date == selectedDate,
                        onClick = {
                           if (day.isInCurrentMonth) {
                              onChangeSelectedDate(it)
                           }
                        },
                        isToday = day.date.toString() == currentDate,
                        mood = mood
                     )
                  }
               }
            }
         }
      }
   }
}

@Composable
fun RowWithDaysOfWeek() {
   val daysOfWeek = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
   Row(
      modifier = Modifier
         .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
   ) {
      daysOfWeek.forEach {
         Text(
            text = it,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
         )
      }
   }
}

@Composable
fun Day(
   modifier: Modifier = Modifier,
   date: CalendarDate,
   isSelected: Boolean,
   isToday: Boolean = false,
   mood: Mood?,
   onClick: (LocalDate) -> Unit,
) {
   val backgroundColor = if (isToday) Sky50 else Color.Transparent
   val textColor = if (date.isInCurrentMonth) Color.Black else Color.LightGray
   val borderColor = if (isSelected) Sky300 else Color.Transparent

   val moodWithIcon = ListMood.entries.find { it.mood == mood?.mood }

   Box(
      modifier = modifier
         .padding(4.dp)
         .aspectRatio(1f)
         .clip(RoundedCornerShape(percent = 50))
         .background(color = backgroundColor)
         .border(
            width = 2.dp,
            color = borderColor,
            shape = RoundedCornerShape(percent = 50)
         )
         .clickable(
            date.isInCurrentMonth
         ) { onClick(date.date) },
      contentAlignment = Alignment.Center
   ) {
      if (moodWithIcon != null) {
         Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = moodWithIcon.icon),
            contentDescription = null
         )
      } else {
         Text(
            text = date.date.dayOfMonth.toString(),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
         )
      }
   }
}

// @Preview(showBackground = true)
// @Composable
// fun CalendarPreview() {
//    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
//    Calendar(selectedDate = selectedDate,
//       listMood = listOf()
//    )
// }
