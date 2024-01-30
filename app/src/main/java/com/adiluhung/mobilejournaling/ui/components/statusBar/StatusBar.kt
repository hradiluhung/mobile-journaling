package com.adiluhung.mobilejournaling.ui.components.statusBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StatusBar(
   modifier : Modifier = Modifier,
   percentage: Float,
   barHeight: Dp = 8.dp,
   barColor: Color = MaterialTheme.colorScheme.primary
) {
   Box(
      modifier = modifier
         .fillMaxWidth()
         .height(barHeight)
         .clip(RoundedCornerShape(percent = 50))
         .background(Color.LightGray)
   ) {
      Box(
         modifier = Modifier
            .fillMaxWidth(percentage)
            .height(barHeight)
            .clip(RoundedCornerShape(percent = 50))
            .background(barColor)
      )
   }
}