package com.github.workspace.snowsample.ui.shape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.unit.IntSize
import com.github.workspace.snowsample.angleSeedRange
import com.github.workspace.snowsample.incrementRange
import com.github.workspace.snowsample.random
import com.github.workspace.snowsample.ui.polygon.Polygon
import kotlin.math.cos
import kotlin.math.sin

class Triangle(
    private val size: Float,
    position: Offset,
    angle: Float
) : ShapeImpl(position, angle) {

    private val polygon: Polygon
        get() = Polygon.makeTriangle(size)

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val path = polygon.makePath(Matrix().apply {
            translate(position.x, position.y, 0f)
        })
        canvas.drawPath(
            path = path,
            paint = paintDelegate
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
        fun createShapeConstructor(size: Float): ShapeConstructor {
            return { offset, angle ->
                Triangle(size, offset, angle)
            }
        }
    }
}