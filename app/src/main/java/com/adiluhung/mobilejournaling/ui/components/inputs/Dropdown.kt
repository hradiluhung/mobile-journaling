package com.adiluhung.mobilejournaling.ui.components.inputs

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Slate300

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
   modifier: Modifier = Modifier,
   label: String,
   placeHolder: String,
   items: List<String>,
   selectedItem: String?,
   onItemSelected: (String) -> Unit,
   leadingIcon: (@Composable () -> Unit)? = null,
   keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
   var expanded by remember { mutableStateOf(false) }
   val context = LocalContext.current

   Column(
      modifier = modifier,
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
      ) {
         Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
               color = Sky900,
               fontWeight = FontWeight.Medium,
               fontSize = 14.sp
            ),
            modifier = Modifier.padding(end = 8.dp)
         )
      }
      Box(
         modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
      ) {

         ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
               expanded = !expanded
            }
         ) {

            OutlinedTextField(
               value = selectedItem ?: "",
               leadingIcon = leadingIcon,
               onValueChange = {},
               placeholder = { Text(text = placeHolder, fontSize = 16.sp) },
               colors = TextFieldDefaults.colors(
                  focusedIndicatorColor = Slate300,
                  unfocusedIndicatorColor = Slate300,
                  unfocusedContainerColor = Color.White,
                  focusedContainerColor = Color.White,
                  unfocusedPlaceholderColor = Slate300,
                  focusedPlaceholderColor = Slate300,
                  focusedLabelColor = Sky900,
                  unfocusedLabelColor = Slate300,
               ),
               textStyle = TextStyle.Default.copy(
                  fontSize = 16.sp,
               ),
               singleLine = true,
               modifier = Modifier
                  .fillMaxWidth()
                  .menuAnchor(),
               trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
               keyboardOptions = keyboardOptions,
            )

            ExposedDropdownMenu(
               expanded = expanded,
               onDismissRequest = { expanded = false }
            ) {
               listOf("L", "P").forEach { item ->
                  DropdownMenuItem(
                     text = { Text(text = item) },
                     onClick = {
                        onItemSelected(item)
                        expanded = false
                     }
                  )
               }
            }
         }
      }
   }
}

@Preview(showBackground = true)
@Composable
fun CustomDropdownPreview() {
   var gender by remember { mutableStateOf("") }

   Box(modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)) {
      CustomDropdown(
         label = "Gender",
         placeHolder = "Pilih Gender",
         items = listOf("L", "P"),
         leadingIcon = {
            Icon(
               painter = painterResource(id = R.drawable.ic_gender),
               contentDescription = null,
               modifier = Modifier.size(24.dp)
            )
         },
         selectedItem = gender,
         onItemSelected = { value ->
            gender = value
         }
      )
   }
}
