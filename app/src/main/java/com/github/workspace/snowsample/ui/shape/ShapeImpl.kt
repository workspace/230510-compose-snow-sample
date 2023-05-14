package com.github.workspace.snowsample.ui.shape

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.unit.IntSize

abstract class ShapeImpl(
    position: Offset,
    angle: Float
) : Shape {

    protected var position by mutableStateOf(position)
    protected var angle by mutableStateOf(angle)

    override fun draw(canvas: Canvas) {
    }

    override fun update(screenSize: IntSize) {
    }
}

typealias ShapeConstructor = (position: Offset, angle: Float) -> ShapeImpl