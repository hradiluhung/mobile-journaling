package com.adiluhung.mobilejournaling.ui.screen.authed.moodCheckIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.utils.greeting

@Composable
fun MoodCheckInScreen(
   navController: NavController,
   viewModel: MoodCheckInViewModel = viewModel(
      factory = ViewModelFactory(context = LocalContext.current)
   )
) {
   var firstName by remember { mutableStateOf<String?>(null) }
   var isLoadingInit by remember { mutableStateOf(true) }
   val listMood = ListMood.entries.toList()

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            isLoadingInit = false
            firstName = uiState.data
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
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
      ) {
         Row(
            modifier = Modifier
               .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
         ) {
            IconButton(
               onClick = {
                  navController.navigateUp()
               }
            ) {
               Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = "Icon"
               )
            }
         }

         Text(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 24.dp),
            text = greeting,
            style = MaterialTheme.typography.bodyMedium.copy(
               fontSize = 16.sp,
               textAlign = TextAlign.Center
            )
         )
         Text(
            modifier = Modifier.fillMaxWidth(),
            text = firstName ?: "Loading...",
            style = MaterialTheme.typography.bodyMedium.copy(
               fontSize = 20.sp,
               textAlign = TextAlign.Center,
               fontWeight = FontWeight.Medium
            )
         )
         Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Bagaimana perasaanmu hari ini?",
            style = MaterialTheme.typography.bodyMedium.copy(
               fontSize = 20.sp,
               textAlign = TextAlign.Center,
               fontWeight = FontWeight.Medium
            )
         )

         Card(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
               containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(4.dp)
         ) {
            Column(
               modifier = Modifier
                  .padding(16.dp)
            ) {
               listMood.chunked(3).forEachIndexed { index, row ->
                  Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.spacedBy(12.dp),
                  ) {
                     row.forEach { mood ->
                        Column(
                           modifier = Modifier
                              .clip(shape = RoundedCornerShape(16.dp))
                              .clickable (
                                 onClick = {
                                    navController.navigate("moodAddNote/${mood.id}")
                                 },
                              )
                              .weight(1f)
                              .border(
                                 width = 1.dp,
                                 color = Color(0xFFECECEC),
                                 shape = RoundedCornerShape(16.dp)
                              )
                              .padding(8.dp)
                        ) {
                           Image(
                              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                              painter = painterResource(id = mood.icon),
                              contentDescription = null
                           )
                           Text(
                              modifier = Modifier.fillMaxWidth(),
                              text = mood.mood,
                              style = MaterialTheme.typography.bodyMedium.copy(
                                 fontSize = 12.sp,
                                 textAlign = TextAlign.Center
                              )
                           )
                        }
                     }
                  }
                  if (index < listMood.chunked(3).size - 1) {
                     Spacer(modifier = Modifier.height(16.dp))
                  }
               }
            }

         }
      }
   }
}

@Preview(showBackground = true)
@Composable
fun MoodCheckInPreview() {
   JournalingTheme {
      MoodCheckInScreen(navController = NavController(LocalContext.current))
   }
}