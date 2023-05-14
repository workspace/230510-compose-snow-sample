package com.github.workspace.snowsample.ui.shape

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.unit.IntSize
interface Shape {
    val paint: Paint
        get() = Paint().apply {
            isAntiAlias = true
            color = Color.White
            style = PaintingStyle.Fill
        }

    fun draw(canvas: Canvas)
    fun update(screenSize: IntSize)
}