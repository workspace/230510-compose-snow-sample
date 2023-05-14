package com.github.workspace.snowsample.ui.shape

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntSize
import com.github.workspace.snowsample.ui.PainterPool
import com.github.workspace.snowsample.ui.effects.Particle

abstract class ShapeImpl(
    position: Offset,
    angle: Float
) : Shape {

    protected var position by mutableStateOf(position)
    protected var angle by mutableStateOf(angle)
    private var particle: Particle? = null

    override fun draw(canvas: Canvas) {
        particle?.let {
            particle?.let {
                draw(canvas, PainterPool.getPainter(it.color))
            }
        }
    }
    open fun draw(canvas: Canvas, paint: Paint) {

    }

    override fun update(screenSize: IntSize) {
    }

    fun attachParticle(particle: Particle) {
        this.particle = particle
    }

    fun applyParticle(ratio: Float) {
        val particle = particle ?: return
        particle.updateProgress(ratio)
        position = position.copy(
            x = particle.currentXPosition,
            y = particle.currentYPosition,
        )
    }
}

typealias ShapeConstructor = (position: Offset, angle: Float) -> ShapeImpl