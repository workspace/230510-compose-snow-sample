package com.github.workspace.snowsample.ui.shape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.unit.IntSize
import com.github.workspace.snowsample.angleSeedRange
import com.github.workspace.snowsample.incrementRange
import com.github.workspace.snowsample.random
import kotlin.math.cos
import kotlin.math.sin

class Rectangle(
    private val size: IntSize,
    position: Offset,
    angle: Float
) : ShapeImpl(position, angle) {

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawRect(
            position.x,
            position.y,
            position.x + size.width.toFloat(),
            position.y + size.height.toFloat(),
            paintDelegate
        )
    }

    override fun update(screenSize: IntSize) {
        val increment = incrementRange.random()
        val xAngle = increment * cos(this.angle)
        val yAngle = increment * sin(this.angle)
        this.angle += angleSeedRange.random() / 1000F
        position = if (position.y > screenSize.height) {
            position.copy(y = 0F)
        } else {
            position.copy(x = position.x + xAngle, y = position.y + yAngle)
        }
    }

    companion object {
        fun createShapeConstructor(size: IntSize): ShapeConstructor {
            return { offset, angle ->
                Rectangle(size, offset, angle)
            }
        }
    }
}