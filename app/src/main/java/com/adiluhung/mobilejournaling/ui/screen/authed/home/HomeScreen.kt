package com.adiluhung.mobilejournaling.ui.screen.authed.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.dummy.dummyProgram
import com.adiluhung.mobilejournaling.ui.components.bottomNavbar.BottomNavbar
import com.adiluhung.mobilejournaling.ui.components.cards.WeeklyMoodCard
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

   // DUMMY DATAS =====================
   val moodData = listOf(
      ListMood.SENANG,
      ListMood.BERSEMANGAT,
      null,
      null,
      ListMood.RAGU,
      ListMood.LELAH,
      ListMood.STRESS
   )
   val sumOfFinishedSession = 30
   val daysStreak = 11

   data class Session(
      val programName: String,
      val moduleName: String,
      val categoryName: String,
      val duration: Int // in minutes
   )

   val nextSession = Session(
      programName = "Program 1",
      moduleName = "Modul 1",
      categoryName = "Kategori 1",
      duration = 10
   )

   val dummyTips =
      "Temukan keseimbangan hidup dan kurangi stres dengan mengintegrasikan waktu untuk istirahat, olahraga, dan refleksi pribadi dalam rutinitas harian Anda."
   // END OF DUMMY DATAS ==============

   val greeting = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
      in 0..11 -> "Selamat Pagi!"
      in 12..15 -> "Selamat Siang!"
      in 16..18 -> "Selamat Sore!"
      in 19..23 -> "Selamat Malam!"
      else -> "Selamat Datang!"
   }

   fun navigateToDetailProgram(programId: Int) {
      navController.navigate("detailProgram/$programId")
   }

   Scaffold(
      bottomBar = { BottomNavbar(navController = navController) }
   ) { innerPadding ->
      BoxWithConstraints {
         Box(
            modifier = Modifier
               .fillMaxWidth()
               .height(180.dp)
               .paint(
                  painterResource(id = R.drawable.background_blur),
                  contentScale = ContentScale.FillBounds
               )
         )

         Column(
            modifier = Modifier
               .padding(
                  top = 16.dp,
                  bottom = innerPadding.calculateBottomPadding(),
                  start = 16.dp,
                  end = 16.dp
               )
               .verticalScroll(rememberScrollState())
               .height(this@BoxWithConstraints.maxHeight)
         ) {
            Text(
               text = greeting,
               style = MaterialTheme.typography.titleLarge,
               color = Sky900
            )

            Text(
               modifier = Modifier.padding(top = 12.dp),
               text = "Narotama Basudara",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp
               ),
               color = Sky900
            )

            WeeklyMoodCard(modifier = Modifier.padding(top = 12.dp), weeklyMoodData = moodData)

            Text(
               modifier = Modifier.padding(top = 12.dp),
               text = "Pencapaian",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp
               ),
               color = Sky900
            )

            Row(
               modifier = Modifier.padding(top = 14.dp),
               horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
               Card(
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(
                     containerColor = Color.White,
                  ),
                  elevation = CardDefaults.cardElevation(2.dp)
               ) {
                  Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.Center,
                  ) {
                     Text(
                        text = sumOfFinishedSession.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 16.sp
                        )
                     )
                     Spacer(modifier = Modifier.width(4.dp))
                     Text(
                        text = "Sesi Selesai",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 16.sp
                        )
                     )
                  }
               }
               Card(
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(
                     containerColor = Color.White,
                  ), elevation = CardDefaults.cardElevation(2.dp)
               ) {
                  Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.Center,
                  ) {
                     Text(
                        text = daysStreak.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 16.sp
                        )
                     )
                     Spacer(modifier = Modifier.width(4.dp))
                     Text(
                        text = "Hari beruntun",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 16.sp
                        )
                     )
                  }

               }
            }

            Text(
               modifier = Modifier.padding(top = 12.dp),
               text = "Sesi Selanjutnya",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp
               ),
               color = Sky900
            )

            Card(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 12.dp),
               shape = RoundedCornerShape(8.dp),
               colors = CardDefaults.cardColors(
                  containerColor = Color.White,
               ),
               elevation = CardDefaults.cardElevation(2.dp)
            ) {
               Row(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center,
               ) {
                  Image(
                     painter = painterResource(id = R.drawable.mood_bersemangat),
                     contentDescription = null,
                     modifier = Modifier.size(48.dp)
                  )
                  Spacer(modifier = Modifier.width(16.dp))
                  Column(
                     modifier = Modifier.weight(1f),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.Start
                  ) {
                     Text(
                        text = nextSession.moduleName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 14.sp
                        )
                     )
                     Text(
                        text = nextSession.programName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 16.sp
                        )
                     )
                     Text(
                        text = nextSession.categoryName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 14.sp
                        )
                     )
                  }
                  Icon(
                     imageVector = Icons.Filled.KeyboardArrowRight,
                     contentDescription = null,
                     modifier = Modifier.size(24.dp),
                     tint = Sky900
                  )
               }
            }
            Card(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 12.dp),
               shape = RoundedCornerShape(8.dp),
               colors = CardDefaults.cardColors(
                  containerColor = Color.White,
               ),
               elevation = CardDefaults.cardElevation(2.dp)
            ) {
               Column(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(16.dp),
               ) {
                  Row(
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_tips),
                        contentDescription = null,
                        tint = Sky900,
                     )
                     Text(
                        text = "Tips Bermanfaat",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 18.sp
                        ),
                        color = Sky900
                     )
                  }
                  Text(
                     modifier = Modifier.padding(top = 8.dp),
                     text = dummyTips,
                     style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                     ),
                     color = Sky900,
                  )
               }
            }

            Row(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 12.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween
            ) {
               Text(
                  text = "Rekomendasi Program",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     fontWeight = FontWeight.Bold,
                     fontSize = 18.sp
                  ),
                  color = Sky900
               )
               ClickableText(text = AnnotatedString("Selengkapnya"), onClick = {})
            }

            // Lazy Vertical Grid with horizontal scroll with contain 3 list of program
            LazyRow(
               modifier = Modifier.padding(top = 12.dp),
               contentPadding = PaddingValues(4.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
               val recommendedProgram = dummyProgram.take(3)
               items(recommendedProgram.size) { index ->
                  val program = recommendedProgram[index]
                  Card(
                     modifier = Modifier
                        .width(120.dp)
                        .height(200.dp),
                     shape = RoundedCornerShape(8.dp),
                     colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                     ),
                     onClick = { navigateToDetailProgram(program.id) }
                  ) {
                     Box{
                        Image(
                           painter = painterResource(
                              id = program.image,
                           ),
                           contentDescription = null,
                           modifier = Modifier.fillMaxSize(),
                           contentScale = ContentScale.Crop
                        )
                     }
                  }
               }
            }

         }
      }
   }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
   JournalingTheme(darkTheme = false) {
      HomeScreen(navController = NavController(LocalContext.current))
   }
}
