package com.adiluhung.mobilejournaling.ui.screen.authed.reminder

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.alarm.AlarmItem
import com.adiluhung.mobilejournaling.alarm.AlarmScheduler
import com.adiluhung.mobilejournaling.alarm.AlarmSchedulerImpl
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.components.buttons.FilledButton
import com.adiluhung.mobilejournaling.ui.components.inputs.ReadonlyTextField
import com.adiluhung.mobilejournaling.ui.components.inputs.TimePickerDialog
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky500
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ReminderScreen(
   navController: NavController,
   viewModel: ReminderViewModel =
      viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {

   val alarmTime = viewModel.alarmTime.observeAsState().value
   var showTimePicker by remember { mutableStateOf(false) }
   val stateTimePicker = rememberTimePickerState()
   var isLoadingSubmit by remember { mutableStateOf(false) }
   var context = LocalContext.current

   val notificationPermissionState = rememberPermissionState(
      permission =
      Manifest.permission.POST_NOTIFICATIONS
   )

   if (!notificationPermissionState.status.isGranted) {
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
                  text = "Setel Pengingat",
                  style = MaterialTheme.typography.displaySmall.copy(
                     color = Sky900,
                     fontSize = 24.sp
                  ),
               )

            }

            Column(
               modifier = Modifier
                  .padding(top = 12.dp)
            ) {
               Text(
                  text = "Gunakan pengingat untuk membantu membentuk rutinitas meditasi kesadaran harian Anda.",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontSize = 14.sp,
                     lineHeight = 16.sp,
                  ),
               )

               Text(
                  text = "Izinkan aplikasi untuk mengirimkan pengingat harian",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontWeight = FontWeight.Bold,
                     fontSize = 16.sp,
                     lineHeight = 20.sp,
                  ),
               )

               Spacer(modifier = Modifier.height(24.dp))

               Button(
                  onClick = {
                     notificationPermissionState.launchPermissionRequest()
                  },
                  modifier = Modifier.fillMaxWidth(),
               ) {
                  Text(
                     text = "Izinkan",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                     )
                  )
               }
            }
         }
      }
   } else {
      var isChecked by remember { mutableStateOf(alarmTime != "") }
      var time by remember { mutableStateOf(alarmTime ?: "") }

      val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)
      var alarmItem: AlarmItem? = null

      fun onUpdateReminder() {
         val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, stateTimePicker.hour)
            set(Calendar.MINUTE, stateTimePicker.minute)
         }

         if (isChecked) {
            alarmItem = AlarmItem(
               alarmTime = calendar,
               message = "Waktunya meditasi"
            )
            alarmItem?.let(alarmScheduler::schedule)

            viewModel.saveAlarm(time)

            Toast.makeText(context, "Pengingat berhasil diatur", Toast.LENGTH_SHORT).show()
         } else {
            viewModel.deleteAlarm()
            alarmItem?.let(alarmScheduler::cancel)
            Toast.makeText(context, "Pengingat berhasil dihapus", Toast.LENGTH_SHORT).show()
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
                  text = "Setel Pengingat",
                  style = MaterialTheme.typography.displaySmall.copy(
                     color = Sky900,
                     fontSize = 24.sp
                  ),
               )

            }

            Column(
               modifier = Modifier
                  .padding(top = 12.dp)
            ) {
               Text(
                  text = "Gunakan pengingat untuk membantu membentuk rutinitas meditasi kesadaran harian Anda.",
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = Sky900,
                     fontSize = 14.sp,
                     lineHeight = 16.sp,
                  ),
               )

               Row(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(top = 24.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  Text(
                     text = "Aktifkan pengingat harian",
                     style = MaterialTheme.typography.bodyMedium.copy(
                        color = Sky900,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                     ),
                  )

                  Switch(
                     checked = isChecked, onCheckedChange = {
                        isChecked = it
                     },
                     colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Sky500.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray.copy(alpha = 0.5f),
                        checkedBorderColor = Color.Transparent,
                        uncheckedBorderColor = Color.Transparent
                     )
                  )

               }

               if (isChecked) {
                  Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                  ) {
                     Text(
                        text = "Atur waktu pengingat",
                        style = MaterialTheme.typography.bodyMedium.copy(
                           color = Sky900,
                           fontWeight = FontWeight.Bold,
                           fontSize = 16.sp,
                           lineHeight = 20.sp,
                        ),
                     )

                     Spacer(modifier = Modifier.width(36.dp))

                     Column {
                        ReadonlyTextField(
                           value = TextFieldValue(text = time),
                           onValueChange = { time = it.text },
                           modifier = Modifier.fillMaxWidth(),
                           onClick = {
                              showTimePicker = true
                           },
                           placeholder = "Pilih waktu",
                        )
                     }
                  }
               }

               if (showTimePicker) {
                  TimePickerDialog(
                     onCancel = {
                        showTimePicker = false
                     },
                     onConfirm = {
                        showTimePicker = false
                        time =
                           "${stateTimePicker.hour.formatWithZero()}:${stateTimePicker.minute.formatWithZero()}"
                     },
                     stateTimePicker = stateTimePicker
                  )
               }

               FilledButton(
                  modifier = Modifier.padding(top = 24.dp),
                  text = "Simpan Pengingat",
                  onClick = {
                     onUpdateReminder()
                  },
                  isLoading = isLoadingSubmit
               )
            }
         }
      }
   }
}

fun Int.formatWithZero(): String {
   return if (this < 10) {
      "0$this"
   } else {
      this.toString()
   }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun Preview() {
   JournalingTheme(darkTheme = false) {
      ReminderScreen(navController = NavController(LocalContext.current))
   }
}
