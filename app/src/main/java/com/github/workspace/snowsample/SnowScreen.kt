package com.github.workspace.snowsample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.github.workspace.snowsample.ui.shape.Circle
import com.github.workspace.snowsample.ui.shape.Rectangle
import com.github.workspace.snowsample.ui.shape.Shape
import com.github.workspace.snowsample.ui.shape.Triangle
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.random.Random

data class ShapeState(
    val snows: List<Shape>
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
                x = canvas.width.random().toFloat(),
                y = canvas.height.random().toFloat()
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
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        while(isActive) {
            awaitFrame()
            for (snow in shapeState.snows) {
                snow.update(screenSize)
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val canvas = drawContext.canvas
        for (snow in shapeState.snows) {
            snow.draw(canvas)
        }
    }
}
