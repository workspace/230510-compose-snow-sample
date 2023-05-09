package com.github.workspace.snowsample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SnowScreen() {
    Canvas(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        drawCircle(color = Color.White, radius = 20F)
    }
}