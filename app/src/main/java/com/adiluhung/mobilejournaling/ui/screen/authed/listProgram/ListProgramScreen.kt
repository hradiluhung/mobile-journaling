package com.adiluhung.mobilejournaling.ui.screen.authed.listProgram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.Program
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.bottomNavbar.BottomNavbar
import com.adiluhung.mobilejournaling.ui.components.dot.Dot
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.components.statusBar.StatusBar
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Yellow400

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProgramScreen(
   navController: NavController,
   viewModel: ListProgramViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   var listOfProgram by remember { mutableStateOf<List<Program>>(emptyList()) }
   var isLoadingInit by remember { mutableStateOf(true) }

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            isLoadingInit = false
            listOfProgram = uiState.data ?: emptyList()
         }

         else -> {
            isLoadingInit = false
         }
      }
   }

   fun navigateToDetailProgram(programId: Int) {
      navController.navigate("detailProgram/$programId")
   }

   Scaffold(
      bottomBar = { BottomNavbar(navController = navController) }
   ) { innerPadding ->
      Box(
         modifier = Modifier
            .fillMaxSize()
            .paint(
               painterResource(id = R.drawable.background_blur),
               contentScale = ContentScale.FillBounds
            )
      )

      LazyVerticalGrid(
         modifier = Modifier
            .padding(
               top = innerPadding.calculateTopPadding(),
               bottom = innerPadding.calculateBottomPadding(),
               start = 16.dp,
               end = 16.dp
            )
            .fillMaxSize(),
         columns = GridCells.Fixed(2),
         verticalArrangement = Arrangement.spacedBy(14.dp),
         horizontalArrangement = Arrangement.spacedBy(14.dp),
      ) {
         item(span = { GridItemSpan(2) }) {
            Text(
               modifier = Modifier.padding(top = 24.dp),
               text = "Program",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 24.sp
               ),
               color = Sky900
            )
         }

         if (isLoadingInit) {
            items(4) {
               Box(
                  modifier = Modifier
                     .fillMaxWidth()
                     .heightIn(min = 245.dp)
                     .clip(RoundedCornerShape(14.dp))
                     .background(shimmerBrush()),
               )
            }
         } else {
            items(listOfProgram.size) { index ->
               val program = listOfProgram[index]
               val totalOfModule = program.modulesCount
               val totalOfSession = program.sessionsCount

               Card(
                  modifier = Modifier
                     .fillMaxWidth()
                     .heightIn(min = 245.dp),
                  shape = RoundedCornerShape(14.dp),
                  colors = CardDefaults.cardColors(
                     containerColor = Color.White,
                  ),
                  elevation = CardDefaults.cardElevation(2.dp),
                  onClick = { navigateToDetailProgram(program.id) }
               ) {
                  Column {
                     Box(
                        modifier = Modifier
                           .fillMaxWidth()
                           .height(120.dp)
                     ) {
                        AsyncImage(
                           model = ImageRequest.Builder(LocalContext.current)
                              .data(program.image)
                              .crossfade(true)
                              .build(),
                           contentDescription = null,
                           contentScale = ContentScale.Crop,
                           modifier = Modifier
                              .fillMaxWidth(),
                        )

                        StatusBar(
                           modifier = Modifier
                              .align(Alignment.BottomCenter)
                              .padding(bottom = 12.dp, start = 12.dp, end = 12.dp),
                           percentage = (program.progress / 100).toFloat(),
                           barColor = Yellow400,
                        )
                     }


                     Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                           text = program.title,
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Bold,
                              fontSize = 16.sp
                           ),
                           color = Sky900,
                           maxLines = 2,
                           overflow = TextOverflow.Ellipsis
                        )
                        Text(
                           modifier = Modifier.padding(top = 4.dp),
                           text = program.description,
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Normal,
                              fontSize = 14.sp
                           ),
                           color = Sky900,
                           maxLines = 2,
                           overflow = TextOverflow.Ellipsis
                        )

                        Row(
                           modifier = Modifier.padding(top = 12.dp),
                           horizontalArrangement = Arrangement.spacedBy(4.dp),
                           verticalAlignment = Alignment.CenterVertically
                        ) {
                           Text(
                              modifier = Modifier.padding(top = 4.dp),
                              text = "$totalOfModule Modul",
                              style = MaterialTheme.typography.bodyMedium.copy(
                                 fontSize = 12.sp,
                                 fontWeight = FontWeight.Bold
                              ),
                              color = Sky900
                           )
                           Dot(modifier = Modifier.padding(top = 4.dp), size = 4.dp)
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
fun ListProgramScreenPreview() {
   JournalingTheme(darkTheme = false) {
      ListProgramScreen(navController = NavController(LocalContext.current))
   }
}