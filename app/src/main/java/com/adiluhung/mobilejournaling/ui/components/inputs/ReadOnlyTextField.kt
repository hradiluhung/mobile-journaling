package com.adiluhung.mobilejournaling.ui.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.adiluhung.mobilejournaling.ui.theme.Sky500

@Composable
fun ReadonlyTextField(
   value: TextFieldValue,
   onValueChange: (TextFieldValue) -> Unit,
   modifier: Modifier = Modifier,
   onClick: () -> Unit,
   placeholder: String? = null
) {
   Box {
      TextField(
         value = value,
         onValueChange = onValueChange,
         modifier = modifier,
         textStyle = TextStyle(
            fontSize = 14.sp,
         ),
         placeholder = {
            placeholder?.let {
               Text(
                  text = it,
                  style = TextStyle(
                     fontSize = 14.sp,
                     color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                  )
               )
            }
         },
      )
      Box(
         modifier = Modifier
            .matchParentSize()
            .alpha(0f)
            .clickable(onClick = onClick),
      )
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
   title: String = "Pilih Waktu",
   onCancel: () -> Unit,
   onConfirm: () -> Unit,
   stateTimePicker: TimePickerState
) {
   Dialog(
      onDismissRequest = onCancel,
      properties = DialogProperties(
         usePlatformDefaultWidth = false
      ),
   ) {
      Card(
         modifier = Modifier.padding(horizontal = 16.dp),
         colors = CardDefaults.cardColors(
            containerColor = Color.White,
         )
      ) {
         Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            Text(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 20.dp),
               text = title,
               style = MaterialTheme.typography.labelMedium
            )

            TimePicker(
               state = stateTimePicker,
            )

            Row(
               modifier = Modifier
                  .height(40.dp)
                  .fillMaxWidth()
            ) {
               Spacer(modifier = Modifier.weight(1f))

               OutlinedButton(
                  onClick = onCancel,
                  border = BorderStroke(1.dp, Sky500),
               ) { Text("Cancel") }

               Spacer(modifier = Modifier.width(8.dp))

               Button(
                  onClick = onConfirm,
                  colors = ButtonDefaults.buttonColors(
                     containerColor = Sky500
                  )
               ) {
                  Text("OK")
               }
            }
         }
      }

   }
}
