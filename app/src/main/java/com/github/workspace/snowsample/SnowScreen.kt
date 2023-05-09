package com.github.workspace.snowsample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle

@Composable
fun SnowScreen() {
    val paint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.White
        style = PaintingStyle.Fill
    }
    Canvas(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        val canvas = drawContext.canvas
        canvas.drawCircle(center, 20f, paint)
//        drawCircle(color = Color.White, radius = 20F)
    }
}