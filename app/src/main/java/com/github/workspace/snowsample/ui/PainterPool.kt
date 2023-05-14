package com.github.workspace.snowsample.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle

object PainterPool {

    private val cached = hashMapOf<Color, Paint>()

    fun getPainter(colorKey: Color): Paint {
        if (cached.containsKey(colorKey)) {
            return cached[colorKey]!!
        }

        val paint = Paint().apply {
            isAntiAlias = true
            color = colorKey
            style = PaintingStyle.Fill
        }
        cached[colorKey] = paint

        return paint

    }
}