package com.adiluhung.mobilejournaling.ui.screen.guest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun StartScreen(navController: NavController) {

    fun navigateToLogin() {
        navController.navigate(Routes.Login.route)
    }

    fun navigateToRegister() {
        navController.navigate(Routes.Register.route)
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
                text = "Meditasi\nLebih Mudah",
                style = MaterialTheme.typography.displaySmall.copy(
                    color = Sky900
                ),
                modifier = Modifier.padding(bottom = 28.dp)
            )

            Text(
                text = "Jelajahi keajaiban ketenangan batin\ndengan Meditasi",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Sky900
                ),
            )

            Column(
                // align to bottom of screen
                modifier = Modifier.fillMaxSize().padding(bottom=36.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                FilledButton(modifier = Modifier.fillMaxWidth(), text = "Daftar Sekarang", onClick = { navigateToRegister()})
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

// preview
@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    JournalingTheme {
        StartScreen(navController = NavController(LocalContext.current))
    }
}
