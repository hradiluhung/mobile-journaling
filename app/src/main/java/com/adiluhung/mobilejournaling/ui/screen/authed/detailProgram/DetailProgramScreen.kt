package com.adiluhung.mobilejournaling.ui.screen.authed.detailProgram

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.ProgramDetail
import com.adiluhung.mobilejournaling.data.network.responses.Session
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.dot.Dot
import com.adiluhung.mobilejournaling.ui.components.statusBar.CircularProgressBar
import com.adiluhung.mobilejournaling.ui.components.statusBar.StatusBar
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Yellow400

@Composable
fun AnimatedBottomSheetSession(
   modifier: Modifier = Modifier,
   session: Session,
   onClose: () -> Unit,
   onClick: () -> Unit
) {
   val colorOverlay = Color.Black.copy(alpha = 0.6f)
   val interactionSource = remember { MutableInteractionSource() }

   Box(
      modifier = modifier
         .zIndex(1f)
         .fillMaxSize(),
   ) {
      // overlay
      Box(
         modifier = modifier
            .fillMaxSize()
            .background(colorOverlay)
            .clickable(
               onClick = onClose,
               indication = null,
               interactionSource = interactionSource
            )
      )

      // content
      Column(
         modifier = Modifier
            .clickable(
               onClick = {},
               indication = null,
               interactionSource = interactionSource
            )
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White)
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(20.dp)
      ) {
         Text(
            text = session.title,
            style = MaterialTheme.typography.bodyMedium.copy(
               fontWeight = FontWeight.Bold,
               fontSize = 18.sp
            ),
            color = Sky900
         )

         Text(
            modifier = Modifier.padding(top = 12.dp),
            text = LoremIpsum(8).values.first(),
            style = MaterialTheme.typography.bodyMedium.copy(
               fontSize = 14.sp
            ),
            color = Sky900
         )

         LazyRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 12.dp)
         ) {
            item {
               Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                     painter = painterResource(id = R.drawable.ic_clock),
                     contentDescription = null
                  )
                  Spacer(modifier = Modifier.width(2.dp))
                  Text(
                     text = session.mediaLength,
                     style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                     ),
                     color = Sky900
                  )
               }
            }

            item {
               Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                     painter = painterResource(id = R.drawable.ic_speaker),
                     contentDescription = null
                  )
                  Spacer(modifier = Modifier.width(2.dp))
                  Text(
                     text = "Audio",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                     ),
                     color = Sky900
                  )
               }
            }

            item {
               Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                     painter = painterResource(id = R.drawable.ic_meditasi),
                     contentDescription = null
                  )
                  Spacer(modifier = Modifier.width(2.dp))
                  Text(
                     text = session.type ?: "Meditasi",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                     ),
                     color = Sky900
                  )
               }
            }
         }

         FilledButton(
            modifier = Modifier.padding(top = 24.dp),
            text = "Mulai",
            onClick = onClick
         )
      }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProgramScreen(
   navController: NavController,
   programId: String,
   viewModel: DetailProgramViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   val context = LocalContext.current
   LaunchedEffect(context) {
      viewModel.getDetailProgram(programId)
   }

   var program by remember { mutableStateOf<ProgramDetail?>(null) }
   var isLoadingInit by remember { mutableStateOf(true) }

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            // show loading
            isLoadingInit = true
         }

         is UiState.Success -> {
            // show success
            program = uiState.data?.data
            isLoadingInit = false
         }

         else -> {
            // show error
            isLoadingInit = false
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

   val totalOfModule = program?.modules?.size ?: 0
   val totalOfSession = program?.modules?.sumOf { it.sessions.size } ?: 0
   val percentageOfCompletion =
      program?.modules?.sumOf { it.sessions.count { it.isCompleted } }?.toFloat()
         ?.div(totalOfSession) ?: 0f
   val colorOverlay = Color.Black.copy(alpha = 0.4f)

   val selectedSession: MutableState<Session?> = remember { mutableStateOf(null) }

   fun onBack() {
      navController.navigateUp()
   }

   fun onOpenBottomSheet(session: Session) {
      selectedSession.value = session
   }

   fun onNavigateToDetailSession() {
      navController.navigate("detailSession/${selectedSession.value?.id}")
   }

   Box(modifier = Modifier.fillMaxSize()) {
      if (program != null) {
         Box(
            modifier = Modifier
               .fillMaxWidth()
               .height(180.dp)
         ) {
            AsyncImage(
               model = ImageRequest.Builder(LocalContext.current)
                  .data(program?.image)
                  .crossfade(true)
                  .build(),
               contentDescription = null,
               contentScale = ContentScale.Crop,
               modifier = Modifier
                  .height(180.dp)
                  .fillMaxWidth(),
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
               .padding(top = 140.dp)
               .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
               .background(Color.White)
               .padding(top = 20.dp, start = 20.dp, end = 20.dp)
               .fillMaxWidth()
               .verticalScroll(rememberScrollState())

         ) {
            Row(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 14.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
            ) {
               Text(
                  text = program?.title ?: "",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     fontWeight = FontWeight.Bold,
                     fontSize = 18.sp
                  ),
                  color = Sky900
               )

               if (program?.isFavorite == true) {
                  Icon(
                     modifier = Modifier.clickable {
                        viewModel.updateFavoriteProgram(program?.id!!)
                     },
                     imageVector = Icons.Filled.Favorite,
                     contentDescription = "Favorite",
                     tint= Sky900,
                  )
               } else {
                  Icon(
                     modifier = Modifier.clickable {
                        viewModel.updateFavoriteProgram(program?.id!!)
                     },
                     imageVector = Icons.Outlined.FavoriteBorder,
                     contentDescription = "Favorite",
                     tint= Sky900,
                  )
               }

            }

            Text(
               modifier = Modifier.padding(top = 12.dp),
               text = program?.description ?: "",
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
               Row(
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

            program?.modules?.forEach { module ->
               Row(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(top = 24.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
               ) {
                  Text(
                     text = module.title,
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
                        text = module.totalDuration,
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
                     onClick = { onOpenBottomSheet(session) }
                  ) {
                     Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                     ) {
                        CircularProgressBar(
                           progress = if(session.isCompleted) 1f else 0f,
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

                        if (session.isFavorite) {
                           Icon(
                              modifier = Modifier.clickable {
                                 viewModel.updateFavoriteSession(session.id)
                              },
                              imageVector = Icons.Filled.Favorite,
                              contentDescription = "Favorite",
                           )
                        } else {
                           Icon(
                              modifier = Modifier.clickable {
                                 viewModel.updateFavoriteSession(session.id)
                              },
                              imageVector = Icons.Outlined.FavoriteBorder,
                              contentDescription = "Favorite"
                           )
                        }

                     }
                  }

                  if (module.sessions.last() != session) {
                     Spacer(modifier = Modifier.height(12.dp))
                  }
               }

               if (program!!.modules.last() == module) {
                  Spacer(modifier = Modifier.height(24.dp))
               }
            }
         }

         if (selectedSession.value != null) {
            AnimatedBottomSheetSession(
               modifier = Modifier.align(Alignment.BottomCenter),
               onClose = {
                  selectedSession.value = null
               },
               session = selectedSession.value!!,
               onClick = {
                  onNavigateToDetailSession()
               }
            )

         }
      }
   }
}

@Preview(showBackground = true)
@Composable
fun DetailProgramScreenPreview() {
   val programId = "2"
   JournalingTheme(darkTheme = false) {
      DetailProgramScreen(
         navController = NavController(LocalContext.current), programId = programId
      )
   }
}
