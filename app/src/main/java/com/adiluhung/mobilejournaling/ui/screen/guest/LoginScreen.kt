package com.adiluhung.mobilejournaling.ui.screen.guest

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomTextField
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.utils.rememberImeState
import com.adiluhung.mobilejournaling.ui.utils.validateForm

@Composable
fun LoginScreen(
   navController: NavController,
   viewModel: AuthViewModel = viewModel(
      factory = ViewModelFactory(context = LocalContext.current)
   )
) {
   var email by remember { mutableStateOf("") }
   var password by remember { mutableStateOf("") }
   var isLoadingSubmit by remember { mutableStateOf(false) }
   val context = LocalContext.current

   val imeState = rememberImeState()
   val scrollState = rememberScrollState()

   LaunchedEffect(key1 = imeState.value) {
      if (imeState.value){
         scrollState.animateScrollTo(scrollState.maxValue, tween(300))
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

   viewModel.uiState.observeAsState().value.let {
      isLoadingSubmit = when (it) {
         is UiState.Loading -> {
            true
         }

         is UiState.Success -> {
            false
         }

         is UiState.Error -> {
            false
         }

         else -> {
            false
         }
      }
   }


   fun navigateToRegister() {
      navController.navigate(Routes.Register.route)
   }

   fun onLogin() {
      viewModel.login(email, password)

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
            .padding(
               top = 12.dp,
               start = 12.dp,
               end = 12.dp,
               bottom = 12.dp
            )
            .verticalScroll(scrollState)
      ) {
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
               text = "Login",
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
            text = "Masuk untuk melanjutkan sesi meditasi",
            style = MaterialTheme.typography.bodyLarge.copy(
               color = Sky900,
               fontSize = 16.sp,
               lineHeight = 24.sp
            ),
         )

         Column(modifier = Modifier.padding(top = 24.dp)) {
            CustomTextField(
               label = "Email",
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
               placeHolder = "********",
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

            Column(
               modifier = Modifier
                  .fillMaxSize()
                  .padding(bottom = 36.dp),
               verticalArrangement = Arrangement.Bottom
            ) {
               FilledButton(
                  modifier = Modifier.padding(top = 24.dp),
                  text = "Login",
                  onClick = {
                     onLogin()
                  },
                  isLoading = isLoadingSubmit,
                  enabled = !isLoadingSubmit && validateForm(listOf(email, password))
               )
               Row(
                  horizontalArrangement = Arrangement.Center, modifier = Modifier
                     .fillMaxWidth()
                     .padding(top = 8.dp, bottom = 8.dp)
               ) {
                  Text(
                     text = "Belum punya akun?",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900
                     ),
                  )
                  ClickableText(
                     text = AnnotatedString("Daftar"),
                     modifier = Modifier.padding(start = 4.dp),
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                     ),
                     onClick = { navigateToRegister() }
                  )
               }
            }

         }
      }
   }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
   JournalingTheme {
      LoginScreen(navController = NavController(LocalContext.current))
   }
}
