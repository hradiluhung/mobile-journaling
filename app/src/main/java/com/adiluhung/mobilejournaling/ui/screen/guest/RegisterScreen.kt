package com.adiluhung.mobilejournaling.ui.screen.guest

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
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
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomTextField
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.utils.validateEmail
import com.adiluhung.mobilejournaling.ui.utils.validateForm
import com.adiluhung.mobilejournaling.ui.utils.validatePassword

@Composable
fun RegisterScreen(
   navController: NavController,
   viewModel: AuthViewModel = viewModel(
      factory = ViewModelFactory(context = LocalContext.current)
   )
) {
   var firstName by remember { mutableStateOf("") }
   var lastName by remember { mutableStateOf("") }
   var email by remember { mutableStateOf("") }
   var password by remember { mutableStateOf("") }
   var isLoading by remember { mutableStateOf(false) }
   val context = LocalContext.current

   fun onRegister() {
      if (!validateEmail(email)) {
         Toast.makeText(context, "Email tidak valid", Toast.LENGTH_SHORT).show()
         return
      }

      if (!validatePassword(password)) {
         Toast.makeText(context, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show()
         return
      }

      viewModel.register(email, password, firstName, lastName)
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

   viewModel.uiState.observeAsState(initial = UiState.Empty).value.let { uiState ->
      when (uiState) {
         is UiState.Empty -> {
            isLoading = false
         }

         is UiState.Loading -> {
            isLoading = true
         }

         is UiState.Success -> {
            isLoading = false
         }

         is UiState.Error -> {
            isLoading = false
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
         Row(modifier = Modifier.padding(top = 48.dp)) {
            IconButton(
               onClick = {
                  navController.navigateUp()
               }
            ) {
               Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = "Back",
                  tint = Sky900
               )
            }

            Text(
               text = "Registrasi Akun",
               style = MaterialTheme.typography.displaySmall.copy(
                  color = Sky900,
                  fontWeight = FontWeight.Bold,
                  fontSize = 24.sp,
                  lineHeight = 30.sp
               ),
               modifier = Modifier.padding(bottom = 18.dp)
            )
         }


         Text(
            text = "Kami menjaga informasi akun tetap aman",
            style = MaterialTheme.typography.bodyLarge.copy(
               color = Sky900,
               fontSize = 16.sp,
               lineHeight = 24.sp
            ),
         )

         Column(modifier = Modifier.padding(top = 24.dp)) {
            CustomTextField(
               label = "Nama Depan",
               placeHolder = "John",
               value = firstName,
               onValueChange = { firstName = it },
               isRequired = true,
               leadingIcon = {
                  Icon(
                     painter = painterResource(id = R.drawable.ic_user),
                     contentDescription = null,
                     modifier = Modifier.size(24.dp)
                  )
               },
               keyboardOptions = KeyboardOptions(
                  imeAction = ImeAction.Next
               )
            )

            CustomTextField(
               modifier = Modifier.padding(top = 8.dp),
               label = "Nama Belakang",
               placeHolder = "Doe",
               value = lastName,
               onValueChange = { lastName = it },
               leadingIcon = {
                  Icon(
                     painter = painterResource(id = R.drawable.ic_user),
                     contentDescription = null,
                     modifier = Modifier.size(24.dp)
                  )
               },
               keyboardOptions = KeyboardOptions(
                  imeAction = ImeAction.Next
               )
            )

            CustomTextField(
               modifier = Modifier.padding(top = 8.dp),
               label = "Email",
               isRequired = true,
               placeHolder = "johndoe@gmail.com",
               value = email,
               onValueChange = { email = it },
               leadingIcon = {
                  Icon(
                     painter = painterResource(id = R.drawable.ic_email),
                     contentDescription = null,
                     modifier = Modifier.size(24.dp)
                  )
               },
               keyboardOptions = KeyboardOptions(
                  imeAction = ImeAction.Next
               )
            )

            CustomTextField(
               modifier = Modifier.padding(top = 8.dp),
               label = "Password",
               placeHolder = "Minimal 8 karakter",
               isRequired = true,
               value = password,
               onValueChange = { password = it },
               leadingIcon = {
                  Icon(
                     painter = painterResource(id = R.drawable.ic_lock),
                     contentDescription = null,
                     modifier = Modifier.size(24.dp)
                  )
               },
               keyboardOptions = KeyboardOptions(
                  imeAction = ImeAction.Done,
                  keyboardType = KeyboardType.Password
               )
            )

            Column {
               FilledButton(
                  modifier = Modifier.padding(top = 24.dp),
                  text = "Daftar",
                  onClick = {
                     onRegister()
                  },
                  isLoading = isLoading,
                  enabled = !isLoading && validateForm(listOf(firstName, email, password))
               )
               Row(
                  horizontalArrangement = Arrangement.Center, modifier = Modifier
                     .fillMaxWidth()
                     .padding(top = 8.dp, bottom = 8.dp)
               ) {
                  Text(
                     text = "Sudah punya akun?",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Light
                     ),
                  )
                  ClickableText(
                     text = AnnotatedString("Masuk"),
                     modifier = Modifier.padding(start = 4.dp),
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                     ),
                     onClick = { navController.navigate(Routes.Login.route) }
                  )
               }
            }

         }
      }
   }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
   JournalingTheme {
      val navController = rememberNavController()
      RegisterScreen(navController = navController)
   }
}

