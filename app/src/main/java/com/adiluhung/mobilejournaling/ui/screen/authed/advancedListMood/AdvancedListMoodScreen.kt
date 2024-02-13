package com.adiluhung.mobilejournaling.ui.screen.authed.advancedListMood

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.Mood
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.flexRow.FlowRow
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky100
import com.adiluhung.mobilejournaling.ui.theme.Sky700
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.utils.formatDateTimeToIndonesian
import com.adiluhung.mobilejournaling.ui.utils.formatMonthToIndonesian
import com.adiluhung.mobilejournaling.ui.utils.nextMonthAndYear
import com.adiluhung.mobilejournaling.ui.utils.prevMonthAndYear


@Composable
fun AdvancedListMoodScreen(
   navController: NavController,
   viewModel: AdvancedListMoodViewModel = viewModel(
      factory = ViewModelFactory(context = LocalContext.current)
   )
) {

   val context = LocalContext.current
   var isLoadingInit by remember { mutableStateOf(true) }
   var listOfMood by remember { mutableStateOf<List<Mood>>(mutableStateListOf()) }
   val currentMonthAndYear = viewModel.currentMonthAndYear.observeAsState().value

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

   Box(
      modifier = with(Modifier) {
         fillMaxSize()
            .paint(
               painterResource(id = R.drawable.screen_background),
               contentScale = ContentScale.FillBounds
            )

      })
   {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
      ) {
         LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            item {
               Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  IconButton(
                     onClick = {
                        navController.navigateUp()
                     }) {
                     Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Sky900
                     )
                  }
                  Text(
                     text = "List Mood Per Bulan",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 24.sp
                     ),
                  )
               }
            }

            item {
               Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  Text(
                     text = formatMonthToIndonesian(currentMonthAndYear!!),
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.SemiBold
                     ),
                  )

                  Row {
                     IconButton(onClick = {
                        viewModel.updateCurrentMonthAndYear(prevMonthAndYear(currentMonthAndYear))
                     }) {
                        Icon(
                           Icons.Default.KeyboardArrowLeft,
                           contentDescription = "Previous Month",
                        )
                     }

                     IconButton(onClick = {
                        viewModel.updateCurrentMonthAndYear(nextMonthAndYear(currentMonthAndYear))
                     }) {
                        Icon(
                           Icons.Default.KeyboardArrowRight,
                           contentDescription = "Next Month",
                        )
                     }

                  }
               }
            }

            if (isLoadingInit) {
               items(4) {
                  Box(
                     modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(shimmerBrush())
                        .height(140.dp)
                  )
               }
            } else {
               if (listOfMood.isNotEmpty()) {
                  items(listOfMood.size) { index ->
                     Card(
                        modifier = Modifier
                           .fillMaxWidth()
                           .border(
                              1.dp,
                              Color(0xFFE0E0E0),
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
                              listOfMood[index].let { mood ->
                                 val moodWithIcon = ListMood.entries.find { it.mood == mood.mood }

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
                              text = listOfMood[index].note,
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
                                 listOfMood[index].tags.forEach { tag ->
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
                  }
               } else {
                  item {
                     Text(
                        text = "Tidak ada mood untuk bulan ini",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           color = Sky900,
                           fontSize = 16.sp,
                           fontStyle = FontStyle.Italic
                        ),
                     )
                  }
               }
            }
         }
      }
   }
}

@Preview(showBackground = true)
@Composable
fun AdvancedListMoodPreview() {
   JournalingTheme {
      AdvancedListMoodScreen(navController = NavController(LocalContext.current))
   }
}