@file:Suppress("UNCHECKED_CAST")

package com.adiluhung.mobilejournaling.ui.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import com.adiluhung.mobilejournaling.ui.theme.Slate300

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeHolder: String,
    value: String,
    isRequired: Boolean = false,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
) {

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
            if(isRequired){
                  Text(
                     text = "*",
                     style = MaterialTheme.typography.bodyMedium.copy(
                           color = Color.Red,
                           fontWeight = FontWeight.Medium,
                           fontSize = 14.sp
                     ),
                  )
            }
        }
        OutlinedTextField(
            value = value,
            leadingIcon = leadingIcon,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeHolder, fontSize = 16.sp) },
            visualTransformation = if(keyboardOptions.keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
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
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
