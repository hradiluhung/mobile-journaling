package com.adiluhung.mobilejournaling.ui.screen.guest

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.inputs.CustomTextField
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun RegisterScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun validateForm(): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 8
    }

    fun navigateToLogin() {
        navController.navigate(Routes.Login.route)
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
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Registrasi Akun",
                style = MaterialTheme.typography.displaySmall.copy(
                    color = Sky900
                ),
                modifier = Modifier.padding(bottom = 18.dp)
            )

            Text(
                text = "Kami menjaga informasi akun tetap aman",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Sky900
                ),
            )

            Column(modifier = Modifier.padding(top = 24.dp)) {
                CustomTextField(
                    label = "Nama Depan",
                    placeHolder = "John",
                    value = firstName,
                    onValueChange = { firstName = it },
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
                        text = "Daftar",
                        onClick = {
                            if (validateForm()) {
                                Log.d("RegisterScreen", "Form is valid")
                            } else {
                                Log.e("RegisterScreen", "Form is not valid")
                            }
                        }
                    )
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)) {
                    Text(
                        text = "Sudah punya akun?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Sky900
                        ),
                    )
                    ClickableText(
                        text = AnnotatedString("Masuk"),
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Sky900,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline
                        ),
                        onClick = {navigateToLogin()}
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
        RegisterScreen(navController = NavController(LocalContext.current))
    }
}

