package com.adiluhung.mobilejournaling.ui.screen.authed.completeProfile

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomDropdown
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomTextField
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun CompleteProfileScreen(
   navController: NavController,
   viewModel: CompleteProfileViewModel = viewModel(
      factory = ViewModelFactory(context = LocalContext.current)
   )
) {
   var isLoadingInit by remember { mutableStateOf(true) }
   var isLoadingSubmit by remember { mutableStateOf(true) }
   var firstName by remember { mutableStateOf("") }
   var lastName by remember { mutableStateOf("") }
   var age by remember { mutableStateOf("") }
   var gender by remember { mutableStateOf("") }
   var schoolName by remember { mutableStateOf("") }
   val context = LocalContext.current

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            isLoadingInit = false
            firstName = uiState.data?.firstName ?: ""
            lastName = uiState.data?.lastName ?: ""
         }

         else -> {
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

   viewModel.updateState.observeAsState().value.let { updateState ->
      when (updateState) {
         is UiState.Loading -> {
            isLoadingSubmit = true
         }

         is UiState.Success -> {
            isLoadingSubmit = false
         }

         is UiState.Error -> {
            isLoadingSubmit = false
         }

         else -> {
            isLoadingSubmit = false
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
            .imePadding()
      })
   {
      Column(modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)) {
         Row(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 36.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
         ) {
            Text(
               text = "Informasi Pribadi",
               style = MaterialTheme.typography.displaySmall.copy(
                  color = Sky900,
                  fontWeight = FontWeight.Bold,
                  fontSize = 24.sp,
                  lineHeight = 30.sp
               ),
            )

            ClickableText(text = AnnotatedString("Lewati"), style = TextStyle.Default.copy(
               fontSize = 16.sp
            ), onClick = {
               viewModel.updateCompleteProfile(true)
               // navigate to Home Screen by replacing the back stack
               navController.navigate(Routes.Home.route) {
                  popUpTo(Routes.Home.route) {
                     inclusive = true
                  }
               }
            })
         }

         Column(
            modifier = Modifier
               .padding(top = 12.dp)
         ) {

            if (isLoadingInit) {
               Box(
                  modifier = Modifier
                     .fillMaxWidth()
                     .height(250.dp)
                     .clip(RoundedCornerShape(8.dp))
                     .background(shimmerBrush())
               )
            } else {
               CustomTextField(
                  label = "Nama Depan",
                  placeHolder = "John",
                  value = firstName,
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Next
                  ),
                  onValueChange = {
                     viewModel.updateName(it, lastName)
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
                  value = lastName,
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Next
                  ),
                  onValueChange = {
                     viewModel.updateName(firstName, it)
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
                  value = schoolName,
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
                  text = "Simpan",
                  onClick = {
                     viewModel.updateProfile(
                        firstName = firstName,
                        lastName = lastName,
                        age = if (age !== "") age.toInt() else 0,
                        gender = gender,
                        schoolName = schoolName
                     )
                  },
                  isLoading = isLoadingSubmit,
                  enabled = !isLoadingSubmit
               )
            }
         }

      }
   }
}

@Preview(showBackground = true)
@Composable
fun CompleteScreenPreview() {
   JournalingTheme {
      val navController = rememberNavController()
      CompleteProfileScreen(navController = navController)
   }
}
