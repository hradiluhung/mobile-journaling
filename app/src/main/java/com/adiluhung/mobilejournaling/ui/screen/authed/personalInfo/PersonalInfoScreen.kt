package com.adiluhung.mobilejournaling.ui.screen.authed.personalInfo

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomDropdown
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomTextField
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.utils.rememberImeState

@Composable
fun PersonalInfoScreen(
   navController: NavController,
   viewModel: PersonalInfoViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   val isLoadingInit = viewModel.isLoadingInit.observeAsState().value ?: true
   val user = viewModel.profileData.observeAsState().value

   var isLoadingSubmit by remember { mutableStateOf(false) }

   val context = LocalContext.current

   val imeState = rememberImeState()
   val scrollState = rememberScrollState()

   LaunchedEffect(key1 = imeState.value) {
      if (imeState.value) {
         scrollState.animateScrollTo(scrollState.maxValue, tween(300))
      }
   }


   viewModel.updateState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingSubmit = true
         }

         is UiState.Success -> {
            isLoadingSubmit = false
            navController.navigateUp()
         }

         else -> {
            isLoadingSubmit = false
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

   Box(
      modifier = with(Modifier) {
         fillMaxSize()
            .paint(
               painterResource(id = R.drawable.screen_background),
               contentScale = ContentScale.FillBounds
            )
            .imePadding()
      })
   {
      Column(
         modifier =
         Modifier
            .padding(top = 36.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
            .verticalScroll(scrollState)
      ) {
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
                  tint = Sky900,
                  modifier = Modifier.size(28.dp)
               )
            }
            Text(
               text = "Informasi Pribadi",
               style = MaterialTheme.typography.displaySmall.copy(
                  color = Sky900,
                  fontSize = 20.sp
               ),
            )

         }

         Column(
            modifier = Modifier
               .padding(top = 12.dp)
         ) {

            if (isLoadingInit && user == null) {
               Box(
                  modifier = Modifier
                     .fillMaxWidth()
                     .height(250.dp)
                     .clip(RoundedCornerShape(8.dp))
                     .background(shimmerBrush())
               )
            } else {
               var firstName by remember { mutableStateOf(user?.firstName) }
               var lastName by remember { mutableStateOf(user?.lastName) }
               var age by remember { mutableStateOf(user?.age.toString()) }
               var gender by remember { mutableStateOf(user?.gender) }
               var schoolName by remember { mutableStateOf(user?.schoolName) }

               CustomTextField(
                  label = "Nama Depan",
                  placeHolder = "John",
                  value = firstName ?: "",
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Next
                  ),
                  onValueChange = {
                     firstName = it
                  },
                  leadingIcon = {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                     )
                  }
               )
               CustomTextField(
                  modifier = Modifier.padding(top = 8.dp),
                  label = "Nama Belakang",
                  placeHolder = "Doe",
                  value = lastName ?: "",
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Next
                  ),
                  onValueChange = {
                     lastName = it
                  },
                  leadingIcon = {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                     )
                  }
               )
               Row(
                  modifier = Modifier.padding(top = 8.dp),
                  horizontalArrangement = Arrangement.spacedBy(14.dp)
               ) {
                  CustomTextField(
                     modifier = Modifier.weight(1f),
                     label = "Umur",
                     placeHolder = "Umur",
                     value = age,
                     keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                     ),
                     onValueChange = {
                        age = it
                     },
                     leadingIcon = {
                        Icon(
                           painter = painterResource(id = R.drawable.ic_birthday),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                        )
                     }
                  )
                  CustomDropdown(
                     modifier = Modifier.weight(2f),
                     label = "Gender",
                     placeHolder = "Pilih Gender",
                     items = listOf("L", "P"),
                     selectedItem = gender,
                     onItemSelected = {
                        gender = it
                     },
                     leadingIcon = {
                        Icon(
                           painter = painterResource(id = R.drawable.ic_gender),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                        )
                     },
                     keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                     )
                  )
               }
               CustomTextField(
                  modifier = Modifier.padding(top = 8.dp),
                  label = "Instansi Pendidikan",
                  placeHolder = "Instansi Pendidikan",
                  value = schoolName ?: "",
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Done
                  ),
                  onValueChange = {
                     schoolName = it
                  },
                  leadingIcon = {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                     )
                  }
               )
               FilledButton(
                  modifier = Modifier.padding(top = 24.dp),
                  text = "Perbarui",
                  onClick = {
                     viewModel.updateProfile(
                        firstName = firstName ?: "",
                        lastName = lastName ?: "",
                        age = age.toInt(),
                        gender = gender ?: "",
                        schoolName = schoolName ?: ""
                     )
                  },
                  isLoading = isLoadingSubmit
               )
            }
         }

      }
   }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
   JournalingTheme(darkTheme = false) {
      PersonalInfoScreen(navController = NavController(LocalContext.current))
   }
}
