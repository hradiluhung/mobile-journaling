package com.adiluhung.mobilejournaling.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilledButton(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit,
   isLoading: Boolean = false,
   gradient: Brush = Brush.horizontalGradient(
      colors = listOf(Color(0xFFB0E3FF), Color(0xFF1580CD))
   ),
   enabled: Boolean = true
) {
   val gradientLoading = Brush.horizontalGradient(
      colors = listOf(
         Color(0xFFAFBACA), Color(0xFFAFBACA)
      )
   )

   Button(
      modifier = modifier,
      colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
      contentPadding = PaddingValues(),
      enabled = enabled,
      shape = RoundedCornerShape(50),
      onClick = { onClick() },
   ) {
      Box(
         modifier = Modifier
            .fillMaxWidth()
            .background(
               if (!enabled) gradientLoading else gradient,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
         contentAlignment = Alignment.Center,
      ) {
         Row(verticalAlignment = Alignment.CenterVertically) {
            if (isLoading) {
               CircularProgressIndicator(
                  color = Color.White,
                  strokeWidth = 2.dp,
                  modifier = Modifier.size(16.dp)
               )

               Spacer(modifier = Modifier.size(8.dp))
            }
            Text(
               text = text,
               color = Color.White,
               style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  lineHeight = 24.sp
               )
            )
         }
      }
   }
}
