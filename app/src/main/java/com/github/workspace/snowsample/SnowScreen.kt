package com.github.workspace.snowsample

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.core.math.MathUtils
import com.github.workspace.snowsample.ui.effects.createParticle
import com.github.workspace.snowsample.ui.shape.Circle
import com.github.workspace.snowsample.ui.shape.Rectangle
import com.github.workspace.snowsample.ui.shape.Shape
import com.github.workspace.snowsample.ui.shape.ShapeImpl
import com.github.workspace.snowsample.ui.shape.Triangle
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.random.Random

data class ShapeState(
    val shapes: List<Shape>
)

fun createShapeList(canvas: IntSize): List<Shape> {

    val constructorList = listOf(
        Rectangle.createShapeConstructor(IntSize(40, 40)),
        Circle.createShapeConstructor(20F),
        Triangle.createShapeConstructor(40f)
    )

    return List(300) {
        val idx = (0..constructorList.lastIndex).random()
        constructorList[idx](
            Offset(
                x = canvas.width / 2f,
                y = canvas.height.toFloat()
            ),
            angleSeed.random() / angleSeed * 0.1F + (PI.toFloat() / 2F)
        )
    }
}

fun Int.random() = Random.nextInt(this)
fun Float.random() = Random.nextFloat() * this
fun ClosedFloatingPointRange<Float>.random() =
    start + Random.nextFloat() * (endInclusive - start)

@Composable
fun SnowScreen() {
    val screenWidth = with(LocalDensity.current) {
        Dp(LocalConfiguration.current.screenWidthDp.toFloat()).roundToPx()
    }
    val screenHeight = with(LocalDensity.current) {
        Dp(LocalConfiguration.current.screenHeightDp.toFloat()).roundToPx()
    }
    val screenSize = IntSize(screenWidth, screenHeight)
    val shapeState by remember {
        mutableStateOf(
            ShapeState(
                createShapeList(
                    IntSize(screenWidth, screenHeight)
                ).map {
                    if (it is ShapeImpl) {
                        it.attachParticle(
                            createParticle(
                                startOffset = Offset(
                                    screenWidth * 0.5f,
                                    screenHeight.toFloat()
                                ),
                                screenSize = screenSize
                            )
                        )
                    }
                    it
                }
            )
        )
    }

    val frameTime = remember { mutableStateOf(0L) }

    val totalMilliseconds = 4000
    var currentFrame = 0L
    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameMillis { frameMs ->
                currentFrame += if (frameTime.value > 0) (frameMs - frameTime.value) else 0
                currentFrame = (currentFrame % totalMilliseconds)
                frameTime.value = frameMs

                val ratio = MathUtils.clamp(currentFrame.toFloat() / totalMilliseconds, 0f, 1f)
                Log.d("HSSS", "ratio : $ratio")
                for (snow in shapeState.shapes) {
//                snow.update(screenSize)
                    if (snow is ShapeImpl) {
                        snow.applyParticle(ratio)
                    }
                }
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val canvas = drawContext.canvas
        for (snow in shapeState.shapes) {
            snow.draw(canvas)
        }
    }
}
