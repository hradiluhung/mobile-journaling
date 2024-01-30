package com.adiluhung.mobilejournaling.ui.screen.authed.detailProgram

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.dummy.dummyProgram
import com.adiluhung.mobilejournaling.ui.components.dot.Dot
import com.adiluhung.mobilejournaling.ui.components.statusBar.CircularProgressBar
import com.adiluhung.mobilejournaling.ui.components.statusBar.StatusBar
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Yellow400

@Composable
fun DetailProgramScreen(navController: NavController, programId: Int) {
   val program = dummyProgram.find { it.id == programId }
   val totalOfModule = program?.modules?.size ?: 0
   val totalOfSession = program?.modules?.sumOf { it.sessions.size } ?: 0
   val percentageOfCompletion = 0.5f
   val colorOverlay = Color.Black.copy(alpha = 0.4f)

   fun onBack() {
      navController.popBackStack()
   }

   Scaffold {
      Box {
         if (program != null) {
            Box(
               modifier = Modifier
                  .fillMaxWidth()
                  .height(180.dp)
            ) {
               Image(
                  painter = painterResource(id = program.image),
                  contentDescription = null,
                  modifier = Modifier
                     .height(180.dp)
                     .fillMaxWidth(),
                  contentScale = ContentScale.Crop,
               )
               Box(
                  modifier = Modifier
                     .fillMaxSize()
                     .background(colorOverlay),
               )
            }

            IconButton(modifier = Modifier
               .padding(top = 24.dp, start = 8.dp), onClick = { onBack() }) {
               Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = "Back",
                  modifier = Modifier
                     .height(28.dp)
                     .width(28.dp),
                  tint = Color.White,
               )
            }

            Column(
               modifier = Modifier
                  .padding(it)
                  .padding(top = 140.dp, bottom = 24.dp)
                  .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                  .background(Color.White)
                  .padding(horizontal = 20.dp)
                  .padding(top = 36.dp)
                  .fillMaxWidth()
                  .verticalScroll(rememberScrollState())

            ) {
               Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  Text(
                     text = program.name,
                     style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                     ),
                     color = Sky900
                  )
                  Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Favorite")
               }

               Text(
                  modifier = Modifier.padding(top = 12.dp),
                  text = program.description,
                  style = MaterialTheme.typography.bodyMedium.copy(
                     fontSize = 14.sp
                  ),
                  color = Sky900
               )

               Row(
                  modifier = Modifier
                     .padding(top = 12.dp)
                     .fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                     Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "$totalOfModule Modul",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontSize = 12.sp,
                           fontWeight = FontWeight.Bold
                        ),
                        color = Sky900
                     )
                     Dot(size = 4.dp)
                     Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "$totalOfSession Sesi",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontSize = 12.sp,
                           fontWeight = FontWeight.Bold
                        ),
                        color = Sky900
                     )
                  }

                  Text(
                     text = "${percentageOfCompletion * 100}%",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.sp
                     ),
                     color = Sky900
                  )
               }

               StatusBar(
                  modifier = Modifier.padding(top = 12.dp),
                  percentage = percentageOfCompletion,
                  barColor = Yellow400
               )

               Spacer(modifier = Modifier.height(18.dp))

               program.modules.forEach { module ->
                  Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                     Text(
                        text = module.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontSize = 18.sp,
                           fontWeight = FontWeight.Bold
                        ),
                        color = Sky900
                     )

                     Row(verticalAlignment = Alignment.CenterVertically) {
                        // clock icon
                        Icon(
                           painter = painterResource(id = R.drawable.ic_clock),
                           contentDescription = null,
                           modifier = Modifier
                              .height(16.dp)
                              .width(16.dp),
                           tint = Sky900,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                           text = "${module.duration} menit",
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontSize = 14.sp,
                              fontWeight = FontWeight.Normal
                           ),
                           color = Sky900
                        )

                     }
                  }

                  module.sessions.forEach { session ->
                     Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                           containerColor = Color.White,
                           contentColor = Sky900
                        ),
                        elevation = CardDefaults.cardElevation(2.dp),
                     ) {
                        Row(
                           modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                           verticalAlignment = Alignment.CenterVertically
                        ) {
                           CircularProgressBar(
                              progress = percentageOfCompletion,
                              strokeWidth = 3.dp,
                              icon = Icons.Filled.PlayArrow
                           )
                           Spacer(modifier = Modifier.width(14.dp))
                           Column(modifier = Modifier.weight(1f)) {
                              Text(
                                 text = session.name,
                                 style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                 ),
                                 color = Sky900
                              )
                              Row(
                                 verticalAlignment = Alignment.CenterVertically,
                                 horizontalArrangement = Arrangement.spacedBy(4.dp)
                              ) {
                                 Text(
                                    text = session.category,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                       fontSize = 12.sp,
                                       fontWeight = FontWeight.Normal
                                    ),
                                    color = Sky900
                                 )
                                 Dot(size = 4.dp)
                                 Text(
                                    text = "${session.duration} Menit",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                       fontSize = 12.sp,
                                       fontWeight = FontWeight.Normal
                                    ),
                                    color = Sky900
                                 )
                              }
                           }
                           Spacer(modifier = Modifier.width(14.dp))
                           Icon(
                              imageVector = Icons.Outlined.FavoriteBorder,
                              contentDescription = "Favorite"
                           )

                        }
                     }

                     if (module.sessions.last() != session) {
                        Spacer(modifier = Modifier.height(12.dp))
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
fun DetailProgramScreenPreview() {
   val programId = 1
   JournalingTheme(darkTheme = false) {
      DetailProgramScreen(
         navController = NavController(LocalContext.current), programId = programId
      )
   }
}
