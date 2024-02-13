package com.adiluhung.mobilejournaling.ui.screen.authed.listMood

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.Mood
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.bottomNavbar.BottomNavbar
import com.adiluhung.mobilejournaling.ui.components.calendar.Calendar
import com.adiluhung.mobilejournaling.ui.components.flexRow.FlowRow
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky100
import com.adiluhung.mobilejournaling.ui.theme.Sky700
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Yellow400
import com.adiluhung.mobilejournaling.ui.theme.Yellow900
import com.adiluhung.mobilejournaling.ui.utils.formatDateTimeToIndonesian
import java.time.LocalDate

@Composable
fun ListMoodScreen(
   navController: NavController,
   viewModel: ListMoodViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   val context = LocalContext.current
   var listOfMood by remember { mutableStateOf<List<Mood>>(mutableStateListOf()) }
   var isLoadingInit by remember { mutableStateOf(true) }
   var isLoadingSelectedMood by remember { mutableStateOf(true) }
   var mood by remember { mutableStateOf<Mood?>(null) }
   val isCheckedIn = viewModel.isCheckedIn.observeAsState().value ?: false

   // Calendar Data
   var selectedDate by remember { mutableStateOf(LocalDate.now()) }

   viewModel.monthlyMood.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            isLoadingInit = false
            listOfMood = uiState.data ?: emptyList()
         }

         is UiState.Error -> {
            isLoadingInit = false
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
         }

         else -> {
            isLoadingInit = false
         }
      }
   }

   viewModel.selectedMood.observeAsState().value.let { moodData ->
      when (moodData) {
         is UiState.Loading -> {
            isLoadingSelectedMood = true
         }

         is UiState.Success -> {
            isLoadingSelectedMood = false
            mood = moodData.data
         }

         is UiState.Error -> {
            isLoadingSelectedMood = false
         }

         else -> {
            isLoadingSelectedMood = false
         }
      }
   }

   Scaffold(
      bottomBar = { BottomNavbar(navController = navController) },
      floatingActionButton = {
         if (!isCheckedIn) {
            Box(
               modifier = Modifier
                  .shadow(
                     elevation = 6.dp,
                     shape = RoundedCornerShape(100.dp),
                     ambientColor = Yellow400
                  )
                  .background(
                     Brush.radialGradient(
                        colors = listOf(Color(0xFFFFE5BB), Color(0xFFFFD48F)),
                        center = Offset(200f, 120f),
                        radius = 100f
                     )
                  )
                  .clip(RoundedCornerShape(100.dp))

            ) {
               Button(
                  colors = ButtonDefaults.buttonColors(
                     containerColor = Color.Transparent,
                  ),
                  onClick = {
                     navController.navigate(Routes.MoodCheckIn.route)
                  },
               ) {
                  Text(
                     text = "Mood CheckIn", style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Yellow900
                     )
                  )
               }
            }
         }
      }
   ) { innerPadding ->
      Box(
         modifier = Modifier
            .fillMaxSize()
            .paint(
               painterResource(id = R.drawable.background_blur),
               contentScale = ContentScale.FillBounds
            )
      ) {
         Column(
            modifier = Modifier
               .padding(
                  top = innerPadding.calculateTopPadding(),
                  bottom = innerPadding.calculateBottomPadding(),
                  start = 16.dp,
                  end = 16.dp
               )
               .verticalScroll(rememberScrollState())
         ) {

            Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
            ) {
               Text(
                  modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
                  text = "Mood Check",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     fontWeight = FontWeight.Bold,
                     fontSize = 24.sp
                  ),
                  color = Sky900
               )

               IconButton(
                  onClick = {
                     navController.navigate(Routes.AdvancedListMood.route)
                  }) {
                  Icon(
                     imageVector = Icons.Default.List,
                     contentDescription = null,
                     tint = Sky900
                  )
               }
            }

            Calendar(
               selectedDate = selectedDate,
               listMood = listOfMood,
               onChangeSelectedDate = { date ->
                  selectedDate = date
                  viewModel.updateCurrentDate(selectedDate.toString())
               },
               onChangeMonthAndYear = { month, year ->
                  viewModel.updateCurrentMonthAndYear("$year-$month")
               }

            )

            Text(
               modifier = Modifier.padding(top = 16.dp),
               text = "Detail Mood",
               style = MaterialTheme.typography.bodyMedium.copy(
                  color = Sky900,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  lineHeight = 24.sp
               ),
            )


            // Selected Mood
            val moodWithIcon = ListMood.entries.find { it.mood == mood?.mood }

            if (isLoadingSelectedMood) {
               Box(
                  modifier = Modifier
                     .padding(top = 12.dp)
                     .fillMaxWidth()
                     .clip(RoundedCornerShape(16.dp))
                     .background(shimmerBrush())
                     .height(140.dp)
               )
            } else {
               if (mood != null) {
                  Card(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp)
                        .border(
                           1.dp, Color(0xFFE0E0E0),
                           RoundedCornerShape(16.dp)
                        ),
                     colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                     ),
                     shape = RoundedCornerShape(16.dp)
                  ) {
                     Column(
                        modifier = Modifier
                           .padding(16.dp)
                           .fillMaxWidth()
                     ) {
                        Row(
                           modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.SpaceBetween,
                           verticalAlignment = Alignment.CenterVertically
                        ) {
                           mood?.let { mood ->
                              Row {
                                 Image(
                                    painter = painterResource(id = moodWithIcon!!.icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                       .padding(end = 16.dp)
                                       .size(24.dp)
                                       .align(Alignment.CenterVertically)
                                 )
                                 Text(
                                    text = mood.mood.lowercase()
                                       .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                       color = Sky900,
                                       fontWeight = FontWeight.Bold,
                                       fontSize = 16.sp,
                                       lineHeight = 24.sp
                                    ),
                                 )
                              }


                              Text(
                                 modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                                 text = formatDateTimeToIndonesian(mood.checkinDate),
                                 style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Sky900,
                                    fontSize = 14.sp
                                 ),
                              )

                           }
                        }

                        Text(
                           modifier = Modifier.fillMaxWidth(),
                           text = mood?.note ?: "",
                           style = MaterialTheme.typography.bodyMedium.copy(
                              color = Sky900,
                              fontSize = 16.sp
                           ),
                        )

                        // tags
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                           FlowRow(
                              horizontalGap = 4.dp,
                              verticalGap = 4.dp,
                           ) {
                              mood?.tags?.forEach { tag ->
                                 val backgroundColor = Color(0xFFF2F5FF)
                                 val textColor = Sky700
                                 val borderColor = Sky100

                                 Text(
                                    text = tag.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                       color = textColor,
                                       fontSize = 14.sp
                                    ),
                                    modifier = Modifier
                                       .background(
                                          color = backgroundColor,
                                          shape = RoundedCornerShape(12.dp)
                                       )
                                       .border(
                                          BorderStroke(1.dp, borderColor),
                                          shape = RoundedCornerShape(12.dp)
                                       )
                                       .clip(RoundedCornerShape(8.dp))
                                       .padding(horizontal = 8.dp, vertical = 4.dp)
                                 )
                              }
                           }
                        }
                     }
                  }
               } else {
                  Text(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                     text = "Tidak ada mood di tanggal ini",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic
                     ),
                  )

               }
            }
         }
      }
   }
}

@Preview(showBackground = true)
@Composable
fun ListMoodScreenPreview() {
   JournalingTheme(darkTheme = false) {
      ListMoodScreen(navController = NavController(LocalContext.current))
   }
}