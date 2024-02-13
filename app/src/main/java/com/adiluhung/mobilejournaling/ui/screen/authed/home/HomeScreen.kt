package com.adiluhung.mobilejournaling.ui.screen.authed.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.AchievementData
import com.adiluhung.mobilejournaling.data.network.responses.RecommendedProgram
import com.adiluhung.mobilejournaling.data.network.responses.UpcomingSession
import com.adiluhung.mobilejournaling.data.network.responses.WeeklyMoodResponse
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.bottomNavbar.BottomNavbar
import com.adiluhung.mobilejournaling.ui.components.cards.WeeklyMoodCard
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Yellow400
import com.adiluhung.mobilejournaling.ui.theme.Yellow900
import com.adiluhung.mobilejournaling.ui.utils.greeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
   navController: NavController,
   viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   var isLoadingInit by remember { mutableStateOf(true) }
   var fullName by remember { mutableStateOf("") }
   var tips by remember { mutableStateOf("") }
   var achievement by remember { mutableStateOf<AchievementData?>(null) }
   var upcomingSession by remember { mutableStateOf<UpcomingSession?>(null) }
   var weeklyMood by remember { mutableStateOf<WeeklyMoodResponse?>(null) }
   var recommendedProgram by remember { mutableStateOf<List<RecommendedProgram>>(emptyList()) }
   var isCheckedIn by remember { mutableStateOf(false) }

   LaunchedEffect(Unit){
      viewModel.getAllData()
   }

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            // Show loading state
            isLoadingInit = true
         }

         is UiState.Success -> {
            // Show success state
            fullName = uiState.data?.userFullName ?: ""
            tips = uiState.data?.tips ?: ""
            achievement = uiState.data?.achievements
            upcomingSession = uiState.data?.upcomingSession
            weeklyMood = uiState.data?.weeklyMood
            recommendedProgram = uiState.data?.recommendedProgram ?: emptyList()
            isCheckedIn = uiState.data?.isCheckedIn ?: false
            isLoadingInit = false
         }

         is UiState.Error -> {
            // Show error state
            isLoadingInit = false
         }

         else -> {
            // Show empty state
            isLoadingInit = false
         }
      }
   }

   fun navigateToDetailProgram(programId: String) {
      navController.navigate("detailProgram/$programId")
   }

   Scaffold(
      topBar = {
         TopAppBar(
            title = {
               Text(
                  text = "Senyummu Perhatianku",
                  style = MaterialTheme.typography.titleMedium.copy(
                     fontWeight = FontWeight.Bold,
                     fontSize = 16.sp
                  )
               )
            },
            colors = TopAppBarDefaults.topAppBarColors(
               containerColor = Color.Transparent,
               titleContentColor = Sky900
            )
         )
      },
      bottomBar = {
         BottomNavbar(navController = navController)
      },
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
               top = innerPadding.calculateTopPadding(),
               bottom = innerPadding.calculateBottomPadding(),
               start = 16.dp,
               end = 16.dp
            )
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
      ) {
         Text(
            text = greeting,
            style = MaterialTheme.typography.titleLarge.copy(
               fontSize = 16.sp,
               fontWeight = FontWeight.Normal,
               lineHeight = 24.sp,
               color = Sky900
            )
         )

         if (isLoadingInit) {
            Box(
               modifier = Modifier
                  .padding(top = 8.dp)
                  .width(200.dp)
                  .height(24.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .background(shimmerBrush())
            )

            Box(
               modifier = Modifier
                  .padding(top = 12.dp)
                  .fillMaxWidth()
                  .height(120.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .background(shimmerBrush())
            )
         } else {
            Text(
               modifier = Modifier.padding(top = 8.dp),
               text = fullName,
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  lineHeight = 24.sp
               ),
               color = Sky900
            )

            WeeklyMoodCard(
               modifier = Modifier.padding(top = 12.dp),
               weeklyMoodData = weeklyMood,
               onClick = {
                  navController.navigate(Routes.ListMood.route) {
                     popUpTo(Routes.Home.route) {
                        saveState = true
                     }
                     launchSingleTop = true
                     restoreState = true
                  }
               }
            )
         }

         Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "Pencapaian",
            style = MaterialTheme.typography.bodyMedium.copy(
               fontWeight = FontWeight.Bold,
               fontSize = 18.sp,
               lineHeight = 20.sp,
               color = Sky900
            ),
         )

         Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
         ) {

            if (isLoadingInit) {
               Box(
                  modifier = Modifier
                     .weight(1f)
                     .height(48.dp)
                     .clip(RoundedCornerShape(16.dp))
                     .background(shimmerBrush())
               )
               Box(
                  modifier = Modifier
                     .weight(1f)
                     .height(48.dp)
                     .clip(RoundedCornerShape(16.dp))
                     .background(shimmerBrush())
               )
            } else {
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
                        text = achievement?.sessionCount?.toString() ?: "0",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 16.sp,
                           color = Sky900,
                        )
                     )
                     Spacer(modifier = Modifier.width(4.dp))
                     Text(
                        text = "Sesi Selesai",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 16.sp,
                           color = Sky900,
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
                        text = achievement?.streak?.toString() ?: "0",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 16.sp,
                           color = Sky900,
                        )
                     )
                     Spacer(modifier = Modifier.width(4.dp))
                     Text(
                        text = "Hari Beruntun",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 16.sp,
                           color = Sky900,
                        )
                     )
                  }

               }
            }
         }

         if (isLoadingInit) {
            Box(
               modifier = Modifier
                  .padding(top = 24.dp)
                  .fillMaxWidth()
                  .height(120.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .background(shimmerBrush())
            )
         } else {
            if (upcomingSession != null) {
               Text(
                  modifier = Modifier.padding(top = 24.dp),
                  text = "Sesi Selanjutnya",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     fontWeight = FontWeight.Bold,
                     fontSize = 18.sp,
                     lineHeight = 20.sp,
                     color = Sky900
                  ),
               )

               Card(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(top = 12.dp),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(
                     containerColor = Color.White,
                  ),
                  elevation = CardDefaults.cardElevation(2.dp),
                  onClick = {
                     navController.navigate("detailSession/${upcomingSession?.id}")
                  }
               ) {
                  Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.Center,
                  ) {
                     Image(
                        painter = painterResource(id = R.drawable.placeholder_next_session),
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
                           text = upcomingSession?.moduleName ?: "Loading...",
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Normal,
                              fontSize = 14.sp
                           )
                        )
                        Text(
                           text = upcomingSession?.title ?: "Loading...",
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Bold,
                              fontSize = 16.sp
                           )
                        )
                        Text(
                           text = upcomingSession?.programName ?: "Loading...",
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
            }
         }

         if (isLoadingInit) {
            Box(
               modifier = Modifier
                  .padding(top = 24.dp)
                  .fillMaxWidth()
                  .height(120.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .background(shimmerBrush())
            )
         } else {
            Card(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 24.dp),
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
                           fontSize = 18.sp,
                           lineHeight = 20.sp,
                           color = Sky900
                        ),
                     )
                  }
                  Text(
                     modifier = Modifier.padding(top = 8.dp),
                     text = tips,
                     style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                     ),
                  )
               }
            }
         }

         Row(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
         ) {
            Text(
               text = "Rekomendasi Program",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  lineHeight = 20.sp,
                  color = Sky900
               ),
            )
            ClickableText(
               text = AnnotatedString("Selengkapnya"),
               onClick = {
                  navController.navigate(Routes.ListProgram.route) {
                     popUpTo(Routes.Home.route) {
                        saveState = true
                     }
                     launchSingleTop = true
                     restoreState = true
                  }
               },
               style = TextStyle(
                  textDecoration = TextDecoration.Underline
               )
            )
         }

         LazyRow(
            modifier = Modifier.padding(top = 12.dp),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
         ) {

            if (isLoadingInit) {
               items(5) {
                  Box(
                     modifier = Modifier
                        .width(120.dp)
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush())
                  )
               }
            } else {
               items(recommendedProgram.size) { index ->
                  val program = recommendedProgram[index]
                  Card(
                     modifier = Modifier
                        .width(120.dp)
                        .height(160.dp),
                     shape = RoundedCornerShape(8.dp),
                     colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                     ),
                     onClick = {
                        navigateToDetailProgram(program.id.toString())
                     }
                  ) {
                     Box {
                        AsyncImage(
                           model = ImageRequest.Builder(LocalContext.current)
                              .data(program.image)
                              .crossfade(true)
                              .build(),
                           contentDescription = null,
                           contentScale = ContentScale.Crop,
                           modifier = Modifier.fillMaxSize(),
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
