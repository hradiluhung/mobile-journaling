package com.adiluhung.mobilejournaling.ui.screen.authed.sessionComplete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.SessionDetail
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.screen.authed.detailSession.DetailSessionViewModel
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun SessionCompleteScreen(
   navController: NavController,
   sessionId: String,
   viewModel: DetailSessionViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
   var session by remember { mutableStateOf<SessionDetail?>(null) }
   var isLoadingInit by remember { mutableStateOf(true) }

   LaunchedEffect(Unit) {
      viewModel.getDetailSession(sessionId)
   }

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            isLoadingInit = false
            session = uiState.data?.data
         }

         is UiState.Error -> {
            isLoadingInit = false
         }

         else -> {
            isLoadingInit = false
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

      })
   {
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(
               top = 12.dp,
               start = 12.dp,
               end = 12.dp,
               bottom = 12.dp
            ),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         if (isLoadingInit) {
            // Loading
            CircularProgressIndicator(
               modifier = Modifier.padding(16.dp),
               color = MaterialTheme.colorScheme.primary
            )
         } else {
            // Content
            Icon(
               modifier = Modifier.size(64.dp),
               imageVector = Icons.Default.CheckCircle,
               contentDescription = null,
               tint = Sky900,
            )

            Text(
               modifier = Modifier.padding(top = 24.dp),
               text = "Selamat, Anda telah menyelesaikan sesi ini!",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontSize = 24.sp,
                  lineHeight = 32.sp,
                  fontWeight = FontWeight.Medium,
                  color = Sky900,
                  textAlign = TextAlign.Center
               )
            )

            Text(
               modifier = Modifier.padding(top = 8.dp),
               text = session?.programName ?: "Loading...",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontSize = 16.sp,
                  lineHeight = 24.sp,
                  fontWeight = FontWeight.Normal,
                  color = Sky900,
                  textAlign = TextAlign.Center
               )
            )
            Text(
               text = session?.title ?: "Loading...",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontSize = 16.sp,
                  lineHeight = 24.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Sky900,
                  textAlign = TextAlign.Center
               )
            )

            FilledButton(
               modifier = Modifier.padding(top = 24.dp),
               text = "Kembali ke Beranda", onClick = {
                  navController.popBackStack(Routes.Home.route, false)
               })
         }
      }
   }


}