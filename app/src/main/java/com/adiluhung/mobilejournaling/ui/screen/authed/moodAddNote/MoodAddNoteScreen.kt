package com.adiluhung.mobilejournaling.ui.screen.authed.moodAddNote

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.Tag
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.flexRow.FlowRow
import com.adiluhung.mobilejournaling.ui.constants.ListMood
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky100
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.utils.getCurrentDateInIndonesian

@Composable
fun MoodAddNoteScreen(
   navController: NavController,
   moodId: String,
   viewModel: MoodAddNoteViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   val context = LocalContext.current
   val mood = ListMood.entries.find { it.id == moodId.toInt() }
   val currentDate = getCurrentDateInIndonesian()
   var listTags by remember { mutableStateOf<List<Tag>>(emptyList()) }
   var note by remember { mutableStateOf("") }
   var selectedTags by remember { mutableStateOf<List<Int>>(emptyList()) }
   var isLoadingInit by remember { mutableStateOf(true) }
   var isLoadingSubmit by remember { mutableStateOf(false) }

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

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            listTags = uiState.data ?: emptyList()
            isLoadingInit = false
         }

         else -> {
            isLoadingInit = false
         }
      }
   }

   viewModel.checkInMessage.observeAsState().value.let { message ->
      when (message) {
         is UiState.Loading -> {
            isLoadingSubmit = true
         }

         is UiState.Success -> {
            isLoadingSubmit = false
            navController.popBackStack(Routes.Home.route, false)
         }

         is UiState.Error -> {
            isLoadingSubmit = false
         }

         else -> {
            isLoadingSubmit = false
         }
      }
   }

   fun onSubmit() {
      if (note.isEmpty()) {
         Toast.makeText(context, "Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show()
         return
      }

      viewModel.checkInMood(
         mood = mood?.mood ?: "",
         note = note,
         tags = selectedTags
      )
   }

   fun onSubmitSkip() {
      note = ""
      selectedTags = emptyList()

      viewModel.checkInMood(
         mood = mood?.mood ?: "",
         note = note,
         tags = selectedTags
      )
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
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
      ) {
         Row(
            modifier = Modifier
               .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
            ClickableText(
               text = AnnotatedString("Lewati"),
               onClick = {
                  onSubmitSkip()
               },
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontSize = 16.sp,
                  color = Sky900,
                  fontWeight = FontWeight.Medium,
                  textDecoration = TextDecoration.Underline

               )
            )
         }

         Row(
            modifier = Modifier
               .padding(top = 24.dp)
               .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
         ) {
            mood?.let { mood ->
               Image(
                  painter = painterResource(id = mood.icon),
                  contentDescription = null,
                  modifier = Modifier
                     .padding(end = 16.dp)
                     .align(Alignment.CenterVertically)
                     .size(56.dp)
               )

               Text(
                  text = "Apa yang membuatmu\nmerasa ${mood.name.lowercase()}?",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontWeight = FontWeight.Medium,
                     fontSize = 20.sp
                  ),
               )
            }
         }

         Card(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 24.dp)
               .border(1.dp, Color(0xFFE0E0E0)),
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
               Row {
                  mood?.let { mood ->
                     Image(
                        painter = painterResource(id = mood.icon),
                        contentDescription = null,
                        modifier = Modifier
                           .padding(end = 16.dp)
                           .size(24.dp)
                           .align(Alignment.CenterVertically)
                     )
                     Text(
                        text = mood.name.lowercase()
                           .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        style = MaterialTheme.typography.bodyMedium.copy(
                           color = Sky900,
                           fontWeight = FontWeight.Bold,
                           fontSize = 16.sp,
                           lineHeight = 24.sp
                        ),
                     )
                  }
               }
               Text(
                  modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                  text = currentDate,
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontSize = 14.sp
                  ),
               )

               TextField(
                  modifier = Modifier.fillMaxWidth(),
                  placeholder = {
                     Text(
                        "Ceritakan harimu disini...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontSize = 16.sp
                        ),
                     )
                  },
                  colors = TextFieldDefaults.colors(
                     focusedContainerColor = Color.Transparent,
                     unfocusedContainerColor = Color.Transparent,
                     focusedIndicatorColor = Color.Transparent,
                     unfocusedIndicatorColor = Color.Transparent,
                  ),
                  value = note,
                  onValueChange = { note = it },
                  textStyle = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontSize = 16.sp
                  ),
               )


               Text(
                  modifier = Modifier.padding(top = 20.dp),
                  text = "Pilih penanda",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontWeight = FontWeight.SemiBold,
                     fontSize = 14.sp,
                  ),
               )

               // tags
               Column(modifier = Modifier.padding(top = 8.dp)) {
                  FlowRow(
                     horizontalGap = 4.dp,
                     verticalGap = 4.dp,
                  ) {
                     listTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag.id)
                        val backgroundColor =
                           if (isSelected) Sky100 else Color.Transparent
                        val textColor = Sky900
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
                                 shape = RoundedCornerShape(8.dp)
                              )
                              .border(
                                 BorderStroke(1.dp, borderColor),
                                 shape = RoundedCornerShape(8.dp)
                              )
                              .clip(RoundedCornerShape(8.dp))
                              .clickable {
                                 selectedTags = if (isSelected) {
                                    selectedTags.filter { it != tag.id }
                                 } else {
                                    selectedTags + tag.id
                                 }
                              }
                              .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                     }
                  }
               }
            }
         }

         FilledButton(
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
            text = "Simpan",
            onClick = {
               onSubmit()
            },
            isLoading = isLoadingSubmit,
            enabled = !isLoadingSubmit && note.isNotEmpty()
         )

      }
   }
}


@Preview(showBackground = true)
@Composable
fun MoodAddNoteScreenPreview() {
   JournalingTheme(darkTheme = false) {
      MoodAddNoteScreen(
         navController = NavController(LocalContext.current), moodId = "2"
      )
   }
}
