package com.github.workspace.snowsample.ui.shape

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.unit.IntSize
import com.github.workspace.snowsample.angleSeedRange
import com.github.workspace.snowsample.incrementRange
import com.github.workspace.snowsample.random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Circle(
    private val size: Float,
    position: Offset,
    angle: Float
) : ShapeImpl(position, angle) {
    private val opacityPaint = Paint().apply {
        isAntiAlias = true
        color = Color.White.copy(alpha = 0.4f)
        style = PaintingStyle.Fill
    }
    private var rotateAngle by mutableStateOf(0.0f)
    private val isFarSide by derivedStateOf {
        val degree = (rotateAngle * 180) % 180 / PI
        degree > 90 && degree < 135
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        super.draw(canvas, paint)
        opacityPaint.color = paint.color
        canvas.drawOval(
            left = position.x,
            right = position.x + size * 2,
            top = position.y,
            bottom = position.y + size * 2 * cos(rotateAngle),
            paint = if (isFarSide) opacityPaint else paint
        )
    }

    override fun update(screenSize: IntSize) {
        val increment = incrementRange.random()
        val xAngle = increment * cos(angle)
        val yAngle = increment * sin(angle)
        angle += angleSeedRange.random() / 1000F
        position = if (position.y < 0) {
            position.copy(y = screenSize.height.toFloat())
        } else {
            position.copy(x = position.x + xAngle, y = position.y - yAngle)
        }
        this.rotateAngle += 0.01f + angleSeedRange.random() / 1000F
//        paintDelegate = if (isFarSide) opacityPaint else paint
    }

    companion object {
        fun createShapeConstructor(size: Float): ShapeConstructor {
            return { offset, angle ->
                Circle(size, offset, angle)
            }
        }
    }
}
