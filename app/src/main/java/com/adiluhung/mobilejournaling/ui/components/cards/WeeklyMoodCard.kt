package com.adiluhung.mobilejournaling.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adiluhung.mobilejournaling.data.network.responses.WeeklyMoodResponse
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyMoodCard(
   modifier: Modifier = Modifier,
   weeklyMoodData: WeeklyMoodResponse?,
   onClick: () -> Unit
) {

   val moodMap = mutableMapOf<String, ListMood?>()
   val startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY)
   for (i in 0 until 7) { // Semua hari dalam seminggu
      val day = startOfWeek.plusDays(i.toLong())
      val dayName = day.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("id", "ID"))
      moodMap[dayName] = null // Tetapkan null untuk setiap hari
   }

   // Memasukkan data mood yang tersedia ke dalam moodMap
   weeklyMoodData?.data?.forEach { (day, dayCheckin) ->
      val moodEnum = ListMood.entries.find { it.mood == dayCheckin.mood }
      moodEnum?.let {
         moodMap[day] = it
      }
   }

   // Langkah kedua: Persingkat kunci nama hari dalam map
   val shortenedMoodMap = moodMap.mapKeys {
      it.key.take(3)
   }

   Card(
      modifier = modifier
         .fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
         containerColor = Color.White,
      ),
      elevation = CardDefaults.cardElevation(4.dp),
      onClick = onClick
   ) {
      Column(modifier = Modifier.padding(12.dp)) {
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
         ) {
            Text(
               text = "Mood Tracker",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Medium,
                  fontSize = 14.sp,
                  lineHeight = 20.sp,
                  color = Sky900
               ),
               color = Sky900
            )
            Icon(
               imageVector = Icons.Filled.KeyboardArrowRight,
               contentDescription = null,
               modifier = Modifier.size(24.dp),
               tint = Sky900
            )
         }

         Row(
            modifier = Modifier.padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
         ) {

            // Iterasi melalui moodMap untuk menampilkan setiap entri mood harian
            shortenedMoodMap.entries.forEachIndexed { index, entry ->
               val mood = entry.value
               val dayName = entry.key

               Card(
                  modifier = Modifier.weight(1f),
                  colors = CardDefaults.cardColors(
                     containerColor = Color.Transparent,
                  )
               ) {
                  Column(
                     modifier = Modifier
                        .fillMaxWidth(),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                     Text(
                        text = dayName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Medium,
                           fontSize = 12.sp,
                           color = Sky900
                        ),
                        maxLines = 1,
                     )
                     Spacer(modifier = Modifier.height(4.dp))
                     mood?.let {
                        Image(
                           painter = painterResource(id = it.icon),
                           contentDescription = null
                        )
                     } ?: run {
                        Box(
                           modifier = Modifier
                              .fillMaxWidth()
                              .aspectRatio(1f)
                              .background(Color(0xFFF5F5F5), CircleShape)
                        )
                     }
                  }
               }
            }
         }

      }
   }
}