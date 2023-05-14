package com.github.workspace.snowsample.ui.shape

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.unit.IntSize

interface Shape {

    fun draw(canvas: Canvas)
    fun update(screenSize: IntSize)
}