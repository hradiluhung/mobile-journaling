package com.adiluhung.mobilejournaling.ui.screen.authed.accountInfo

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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomTextField
import com.adiluhung.mobilejournaling.ui.components.loadingEffect.shimmerBrush
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun AccountInfoScreen(
   navController: NavController,
   viewModel: AccountInfoViewModel = viewModel(
      factory = ViewModelFactory(
         LocalContext.current
      )
   )
) {
   val userData = viewModel.profileData.observeAsState().value
   val isLoadingInit = viewModel.isLoadingInit.observeAsState().value ?: true
   var isLoadingSubmit by remember { mutableStateOf(false) }
   val context = LocalContext.current

   viewModel.updateState.observeAsState().value?.let { state ->
      when (state) {
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
         modifier = Modifier.padding(
            top = 36.dp,
            start = 12.dp,
            end = 12.dp,
            bottom = 12.dp
         )
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
                  tint = Sky900
               )
            }
            Text(
               text = "Informasi Akun",
               style = MaterialTheme.typography.displaySmall.copy(
                  color = Sky900
               ),
            )

         }

         Column(
            modifier = Modifier
               .padding(top = 12.dp)
         ) {
            if (isLoadingInit && userData == null) {
               Box(
                  modifier = Modifier
                     .fillMaxWidth()
                     .height(250.dp)
                     .clip(RoundedCornerShape(8.dp))
                     .background(shimmerBrush())
               )
            } else {
               var email by remember { mutableStateOf(userData?.email) }
               var tempEmail by remember { mutableStateOf(userData?.email) }
               var password by remember { mutableStateOf("") }
               var passwordConfirmation by remember { mutableStateOf("") }

               fun onUpdateProfile() {
                  if (email != tempEmail && password.isEmpty() && passwordConfirmation.isEmpty()) {
                     Toast.makeText(
                        context,
                        "Email harus berbeda dari sebelumnya",
                        Toast.LENGTH_SHORT
                     ).show()
                     return
                  }

                  viewModel.updateEmailAndPassword(
                     email = email ?: "",
                     password = password,
                     passwordConfirmation = passwordConfirmation
                  )
               }

               CustomTextField(
                  label = "Email",
                  placeHolder = "johndoe@gmail.com",
                  value = email ?: "",
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Done
                  ),
                  onValueChange = {
                     email = it
                  },
                  leadingIcon = {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                     )
                  }
               )

               Text(
                  modifier = Modifier.padding(top = 16.dp),
                  text = "Ubah Kata sandi",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontWeight = FontWeight.Bold,
                     fontSize = 16.sp,
                     lineHeight = 20.sp
                  ),
               )
               Text(
                  modifier = Modifier.padding(top = 8.dp),
                  text = "Biarkan kosong apabila tidak ingin mengganti kata sandi",
                  style = MaterialTheme.typography.bodySmall.copy(
                     color = Sky900,
                     fontSize = 14.sp,
                     lineHeight = 16.sp
                  ),
               )
               CustomTextField(
                  modifier = Modifier.padding(top = 12.dp),
                  label = "Password",
                  placeHolder = "Minimal 8 Karakter",
                  value = password,
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Next
                  ),
                  onValueChange = {
                     password = it
                  },
                  leadingIcon = {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                     )
                  }
               )
               CustomTextField(
                  modifier = Modifier.padding(top = 12.dp),
                  label = "Konfirmasi Password",
                  placeHolder = "Ulangi Password",
                  value = passwordConfirmation,
                  keyboardOptions = KeyboardOptions.Default.copy(
                     imeAction = ImeAction.Done
                  ),
                  onValueChange = {
                     passwordConfirmation = it
                  },
                  leadingIcon = {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                     )
                  }
               )

               FilledButton(
                  modifier = Modifier.padding(top = 24.dp),
                  text = "Perbarui",
                  onClick = {
                     onUpdateProfile()
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
      AccountInfoScreen(navController = NavController(LocalContext.current))
   }
}