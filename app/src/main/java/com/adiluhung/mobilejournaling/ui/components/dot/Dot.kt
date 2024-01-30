package com.adiluhung.mobilejournaling.ui.components.dot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.adiluhung.mobilejournaling.ui.theme.Sky900

@Composable
fun Dot(modifier: Modifier = Modifier, size: Dp, color: Color = Sky900) {
   Box(
      modifier = modifier
         .size(size)
         .background(color, shape = CircleShape)
   )
}