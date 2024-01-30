package com.adiluhung.mobilejournaling.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme

@Composable
fun FilledButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFB0E3FF), Color(0xFF1580CD)),
                    // center top right
                    center = Offset(800f, -300f),
                    radius = 700f
                )
            )
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
            // modifier = Modifier
            //     .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview
@Composable
fun FilledButtonPreview() {
    JournalingTheme {
        FilledButton(
            text = "Mulai",
            onClick = {}
        )
    }
}