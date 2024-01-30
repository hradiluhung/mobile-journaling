package com.adiluhung.mobilejournaling.ui.components.statusBar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adiluhung.mobilejournaling.ui.theme.Sky50
import com.adiluhung.mobilejournaling.ui.theme.Sky500
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun CircularProgressBar(
   progress: Float,
   modifier: Modifier = Modifier,
   progressBarColor: Color = Sky500,
   iconColor: Color = Sky900,
   backgroundColor: Color = Sky50,
   strokeWidth: Dp = 8.dp,
   icon: ImageVector
) {
   Box(
      modifier = modifier
         .wrapContentSize()
         .size(42.dp)
   ) {
      Canvas(
         modifier = Modifier.fillMaxSize(),
         onDraw = {
            val outerRadius = size.minDimension / 2
            val innerRadius = outerRadius - strokeWidth.toPx()
            val centerX = size.width / 2
            val centerY = size.height / 2

            // Draw progress arc
            drawArc(
               color = progressBarColor,
               startAngle = -90f,
               sweepAngle = progress * 360,
               useCenter = false,
               style = Stroke(width = strokeWidth.toPx())
            )

            // Draw icon in center
            drawCircle(
               color = backgroundColor,
               radius = outerRadius,
               center = center
            )
         }
      )
      // Icon in center
      Icon(
         imageVector = icon,
         contentDescription = null,
         tint = iconColor,
         modifier = Modifier
            .fillMaxSize()
      )
   }
}