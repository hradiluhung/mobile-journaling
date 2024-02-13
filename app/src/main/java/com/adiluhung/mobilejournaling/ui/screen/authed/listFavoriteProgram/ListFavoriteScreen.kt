package com.adiluhung.mobilejournaling.ui.screen.authed.listFavoriteProgram

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.adiluhung.mobilejournaling.data.network.responses.SessionDetail
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.bottomNavbar.BottomNavbar
import com.adiluhung.mobilejournaling.ui.components.dot.Dot
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.components.statusBar.CircularProgressBar
import com.adiluhung.mobilejournaling.ui.components.statusBar.StatusBar
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Yellow400

enum class FavoriteTab(val tabName: String) {
   PROGRAM("Program"),
   SESSION("Sesi")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListFavoriteScreen(
   navController: NavController,
   viewModel: ListFavoriteViewModel =
      viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   val context = LocalContext.current
   var listOfFavoriteProgram by remember { mutableStateOf<List<Program>>(emptyList()) }
   var listOfFavoriteSession by remember { mutableStateOf<List<SessionDetail>>(emptyList()) }
   var isLoadingInitProgram by remember { mutableStateOf(true) }
   var isLoadingInitSession by remember { mutableStateOf(true) }

   val listOfTabs = FavoriteTab.entries
   var selectedTabs by remember { mutableStateOf(FavoriteTab.PROGRAM) }

   viewModel.listFavoriteProgram.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInitProgram = true
         }

         is UiState.Success -> {
            isLoadingInitProgram = false
            listOfFavoriteProgram = uiState.data ?: emptyList()
         }

         else -> {
            isLoadingInitProgram = false
         }
      }
   }

   viewModel.listFavoriteSession.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInitSession = true
         }

         is UiState.Success -> {
            isLoadingInitSession = false
            listOfFavoriteSession = uiState.data ?: emptyList()
         }

         else -> {
            isLoadingInitSession = false
         }
      }
   }

   LaunchedEffect(Unit) {
      viewModel
         .toastMessage
         .collect { message ->
            Toast.makeText(
               context,
               message,
               Toast.LENGTH_SHORT,
            ).show()
         }
   }

   fun navigateToDetailProgram(programId: Int) {
      navController.navigate("detailProgram/$programId")
   }

   fun navigateToDetailSession(sessionId: Int) {
      navController.navigate("detailSession/$sessionId")
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
      ) {
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
                  text = "Favorit",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     fontWeight = FontWeight.Bold,
                     fontSize = 24.sp
                  ),
                  color = Sky900
               )
            }

            item(span = { GridItemSpan(2) }) {
               Row(
                  modifier = Modifier.padding(top = 12.dp),
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
               ) {
                  listOfTabs.forEach { tab ->
                     Card(
                        shape = RoundedCornerShape(percent = 50),
                        colors = CardDefaults.cardColors(
                           containerColor = if (tab == selectedTabs) Sky900 else Color.Transparent
                        ),
                        border = BorderStroke(
                           width = 1.dp,
                           color = if (tab != selectedTabs) Sky900 else Color.Transparent,
                        ),
                        onClick = {
                           selectedTabs = tab
                        }
                     ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                           Text(
                              text = tab.tabName,
                              style = MaterialTheme.typography.bodyMedium.copy(
                                 fontWeight = FontWeight.Bold,
                                 fontSize = 14.sp
                              ),
                              color = if (tab == selectedTabs) Color.White else Sky900
                           )
                        }
                     }
                  }
               }
            }

            if (selectedTabs == FavoriteTab.PROGRAM) {
               if (isLoadingInitProgram) {
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
                  if (listOfFavoriteProgram.isNotEmpty()) {
                     items(listOfFavoriteProgram.size) { index ->
                        val program = listOfFavoriteProgram[index]
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
                  } else {
                     item(span = { GridItemSpan(2) }) {
                        Text(
                           modifier = Modifier
                              .fillMaxWidth()
                              .padding(top = 12.dp),
                           text = "Belum ada program favorit",
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
            } else {
               if (isLoadingInitSession) {
                  for (i in 0 until 4) {
                     item(span = { GridItemSpan(2) }) {
                        Box(
                           modifier = Modifier
                              .fillMaxWidth()
                              .heightIn(min = 80.dp)
                              .clip(RoundedCornerShape(14.dp))
                              .background(shimmerBrush()),
                        )
                     }
                  }
               } else {
                  if (listOfFavoriteSession.isNotEmpty()) {
                     listOfFavoriteSession.forEach { session ->

                        item(span = { GridItemSpan(2) }) {
                           Card(
                              modifier = Modifier.fillMaxWidth(),
                              colors = CardDefaults.cardColors(
                                 containerColor = Color.White,
                                 contentColor = Sky900
                              ),
                              elevation = CardDefaults.cardElevation(2.dp),
                              onClick = {
                                 navigateToDetailSession(session.id)
                              }
                           ) {
                              Row(
                                 modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                 verticalAlignment = Alignment.CenterVertically
                              ) {
                                 CircularProgressBar(
                                    progress = if (session.isCompleted) 1f else 0f,
                                    strokeWidth = 3.dp,
                                    icon = Icons.Filled.PlayArrow
                                 )
                                 Spacer(modifier = Modifier.width(14.dp))
                                 Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                       text = session.title,
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
                                          text = session.type ?: "",
                                          style = MaterialTheme.typography.bodyMedium.copy(
                                             fontSize = 12.sp,
                                             fontWeight = FontWeight.Normal
                                          ),
                                          color = Sky900
                                       )
                                       Dot(size = 4.dp)
                                       Text(
                                          text = session.mediaLength,
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
                                    modifier = Modifier.clickable {
                                       viewModel.updateFavoriteSession(session.id)
                                    },
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Favorite",
                                 )

                              }
                           }
                        }
                     }

                  } else {
                     item(span = { GridItemSpan(2) }) {
                        Text(
                           modifier = Modifier
                              .fillMaxWidth()
                              .padding(top = 12.dp),
                           text = "Belum ada sesi favorit",
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
   }

}


@Preview(showBackground = true)
@Composable
fun ListFavoriteScreenPreview() {
   JournalingTheme(darkTheme = false) {
      ListFavoriteScreen(navController = NavController(LocalContext.current))
   }
}