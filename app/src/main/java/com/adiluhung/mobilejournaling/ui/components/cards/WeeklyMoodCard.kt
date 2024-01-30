package com.adiluhung.mobilejournaling.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun WeeklyMoodCard(modifier: Modifier = Modifier, weeklyMoodData: List<ListMood?>) {
   val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

   Card(
      modifier = modifier
         .fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
         containerColor = Color.White,
      ),
      elevation = CardDefaults.cardElevation(4.dp)
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
                  fontSize = 14.sp
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

         LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
         ) {
            items(weeklyMoodData.size) { index ->
               val mood = weeklyMoodData[index]
               if (mood != null) {
                  Card(
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
                           text = days[index],
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Medium,
                              fontSize = 12.sp
                           ),
                           color = Sky900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Image(
                           painter = painterResource(id = mood.icon),
                           contentDescription = null
                        )
                     }
                  }
               } else {
                  Card(
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
                           text = days[index],
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Medium,
                              fontSize = 12.sp
                           ),
                           color = Sky900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                           modifier = Modifier
                              .fillMaxWidth()
                              .aspectRatio(1f)
                              .background(Color.Gray, CircleShape)
                        )
                     }
                  }
               }
            }
         }

      }
   }
}