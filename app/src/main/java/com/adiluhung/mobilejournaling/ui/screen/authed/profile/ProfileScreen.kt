package com.adiluhung.mobilejournaling.ui.screen.authed.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.DataProfile
import com.adiluhung.mobilejournaling.route.Routes
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.components.bottomNavbar.BottomNavbar
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
   navController: NavController,
   viewModel: ProfileViewModel = viewModel(
      factory = ViewModelFactory(context = LocalContext.current)
   )
) {
   var isLoadingInit by remember { mutableStateOf(true) }
   var userData by remember { mutableStateOf<DataProfile?>(null) }
   var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
   val alarmTime = viewModel.alarmTime.observeAsState().value
   var time by remember { mutableStateOf(alarmTime ?: "") }

   val galleryLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
         // process eith the received image uri
         selectedImageUri = uri
      }


   val context = LocalContext.current
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

   viewModel.userData.observeAsState().value.let { state ->
      when (state) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            isLoadingInit = false
            userData = state.data?.data
         }

         is UiState.Error -> {
            isLoadingInit = false
         }

         else -> {
            isLoadingInit = false
         }
      }
   }

   viewModel.updatePhoto.observeAsState().value.let { state ->
      if (state !is UiState.Loading && state !is UiState.Empty) {
         selectedImageUri = null
      }
   }

   fun onLogout() {
      viewModel.logout()
      // navigate to Start Screen by replacing the back stack
      navController.navigate(Routes.Start.route) {
         popUpTo(Routes.Start.route) {
            inclusive = true
         }
      }
   }

   val timeStamp: String = SimpleDateFormat(
      "dd-MMM-yyyy",
      Locale.getDefault()
   ).format(System.currentTimeMillis())

   fun createCustomTempFile(context: Context): File {
      val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
      return File.createTempFile(timeStamp, ".jpg", storageDir)
   }

   fun uriToFile(selectedImg: Uri, context: Context): File {
      val contentResolver: ContentResolver = context.contentResolver
      val myFile = createCustomTempFile(context)

      val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
      val outputStream: OutputStream = FileOutputStream(myFile)
      val buf = ByteArray(1024)
      var len: Int
      while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
      outputStream.close()
      inputStream.close()
      return myFile
   }

   Scaffold(
      bottomBar = { BottomNavbar(navController = navController) }
   ) { innerPadding ->
      Box(
         modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .paint(
               painterResource(id = R.drawable.background_profile),
               contentScale = ContentScale.FillBounds
            )
      )
      Column(
         modifier = Modifier
            .padding(
               top = innerPadding.calculateTopPadding(),
               bottom = innerPadding.calculateBottomPadding(),
               start = 16.dp,
               end = 16.dp
            )
      ) {
         Row(
            modifier = Modifier
               .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
         ) {
            Text(
               modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
               text = "Profil",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 24.sp
               ),
               color = Sky900
            )

            IconButton(onClick = {
               onLogout()
            }) {
               Icon(
                  painterResource(id = R.drawable.ic_logout),
                  contentDescription = "Logout"
               )

            }
         }

         Column(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            Box {
               AsyncImage(
                  model = ImageRequest.Builder(LocalContext.current)
                     .data(
                        userData?.photoUrl
                           ?: selectedImageUri
                           ?: "https://ui-avatars.com/api/?name=${userData?.firstName + " " + userData?.lastName}&background=random&color=fff"
                     )
                     .crossfade(true)
                     .build(),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                     .height(100.dp)
                     .aspectRatio(1f)
                     .clip(RoundedCornerShape(percent = 50)),
               )

               // IconButton(
               //    onClick = {
               //       galleryLauncher.launch("image/*")
               //    },
               //    colors = IconButtonDefaults.iconButtonColors(
               //       containerColor = Color.White,
               //    ),
               //    modifier = Modifier
               //       .align(Alignment.BottomEnd)
               //       .padding(top = 2.dp)
               // ) {
               //    Icon(
               //       imageVector = Icons.Default.Edit,
               //       contentDescription = "Edit"
               //    )
               // }
            }

            if (selectedImageUri != null) {
               Button(
                  modifier = Modifier.padding(top = 4.dp),
                  onClick = {
                     val myFile = uriToFile(selectedImageUri!!, context)
                     viewModel.uploadImage(myFile)
                  },
                  colors = ButtonDefaults.buttonColors(
                     containerColor = Sky900
                  ),
               ) {
                  Text("Upload Gambar")
               }
            }

            Text(
               modifier = Modifier.padding(top = 24.dp),
               text = if (userData != null) "${userData?.firstName} ${userData?.lastName}" else "Loading...",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 24.sp
               ),
               color = Sky900
            )
            Text(
               modifier = Modifier.padding(top = 4.dp),
               text = if (userData != null) "${userData?.email}" else "Loading...",
               style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Normal,
                  fontSize = 16.sp,
                  color = Sky900
               ),
            )

            Card(
               modifier = Modifier
                  .padding(top = 12.dp)
                  .fillMaxWidth(),
               colors = CardDefaults.cardColors(
                  containerColor = Color.White
               ),
               elevation = CardDefaults.cardElevation(2.dp),
               shape = RoundedCornerShape(16.dp),
               onClick = {
                  navController.navigate(Routes.Reminder.route)
               }

            ) {
               Row(
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  Image(
                     painter = painterResource(id = R.drawable.clock),
                     contentDescription = "Email",
                     modifier = Modifier
                        .height(45.dp)
                        .aspectRatio(1f),
                     contentScale = ContentScale.FillBounds
                  )

                  Spacer(modifier = Modifier.width(8.dp))

                  Column(
                     modifier = Modifier
                        .weight(1f),
                  ) {
                     Text(
                        text = if(time== "") "Mulai rutinitas dengan" else "Pengingat aktif pukul",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Light,
                           fontSize = 14.sp,
                           lineHeight = 16.sp
                        ),
                        color = Sky900
                     )
                     Text(
                        text = if(time == "") "Pengingat" else time,
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Medium,
                           fontSize = 16.sp,
                           lineHeight = 20.sp
                        ),
                        color = Sky900
                     )
                  }

                  Icon(
                     imageVector = Icons.Default.KeyboardArrowRight,
                     contentDescription = null,
                     tint = Sky900,
                     modifier = Modifier.size(24.dp)
                  )
               }
            }

            Card(
               modifier = Modifier
                  .padding(top = 12.dp)
                  .fillMaxWidth(),
               colors = CardDefaults.cardColors(
                  containerColor = Color.White
               ),
               elevation = CardDefaults.cardElevation(2.dp),
               shape = RoundedCornerShape(16.dp)

            ) {
               Column {
                  Row(
                     modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                           navController.navigate(Routes.PersonalInfo.route)
                        }
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
                     verticalAlignment = Alignment.CenterVertically
                  ) {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_personal),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                     )

                     Spacer(modifier = Modifier.width(8.dp))

                     Text(
                        text = "Informasi Pribadi",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 16.sp,
                           lineHeight = 20.sp
                        ),
                        color = Sky900,
                        modifier = Modifier.weight(1f)
                     )
                  }

                  Spacer(modifier = Modifier.height(8.dp))

                  Row(
                     modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                           navController.navigate(Routes.AccountInfo.route)
                        }
                        .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                     verticalAlignment = Alignment.CenterVertically
                  ) {
                     Icon(
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                     )

                     Spacer(modifier = Modifier.width(8.dp))

                     Text(
                        text = "Informasi Akun",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           fontWeight = FontWeight.Normal,
                           fontSize = 16.sp,
                           lineHeight = 20.sp
                        ),
                        color = Sky900,
                        modifier = Modifier.weight(1f)
                     )
                  }

               }
            }
         }
      }

   }

}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
   JournalingTheme(darkTheme = false) {
      ProfileScreen(navController = NavController(LocalContext.current))
   }
}
