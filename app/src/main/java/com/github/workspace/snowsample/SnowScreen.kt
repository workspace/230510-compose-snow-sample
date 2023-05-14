package com.github.workspace.snowsample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class SnowState(
    val snows: List<Snow>
)

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

class Snow(
    val size: Float,
    position: Offset,
    angle: Float
) : Shape {
    override val paint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.White
        style = PaintingStyle.Fill
    }
    private var position by mutableStateOf(position)
    private var angle by mutableStateOf(angle)
    private var rotateAngle by mutableStateOf(0.0f)

    override fun draw(canvas: Canvas) {
        canvas.drawOval(
            left = position.x,
            right = position.x + size,
            top = position.y,
            bottom = position.y + size * cos(rotateAngle),
            paint = paint
        )
    }

    override fun update(screenSize: IntSize) {
        val increment = incrementRange.random()
        val xAngle = increment * cos(angle)
        val yAngle = increment * sin(angle)
        angle += angleSeedRange.random() / 1000F
        position = if (position.y > screenSize.height) {
            position.copy(y = 0F)
        } else {
            position.copy(x = position.x + xAngle, y = position.y + yAngle)
        }
        this.rotateAngle += 0.01f + angleSeedRange.random() / 1000F
    }
}

private const val angleSeed = 25F
private val angleSeedRange = -angleSeed..angleSeed
private val incrementRange = 0.4F..0.8F


fun createSnowList(canvas: IntSize): List<Snow> {
    return List(300) {
        Snow(
            size = 20F,
            position = Offset(
                x = canvas.width.random().toFloat(),
                y = canvas.height.random().toFloat()
            ),
            angle = angleSeed.random() / angleSeed * 0.1F + (PI.toFloat() / 2F),
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
    var snowState by remember {
        mutableStateOf(
            SnowState(
                createSnowList(
                    IntSize(screenWidth, screenHeight)
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            awaitFrame()
            for (snow in snowState.snows) {
                snow.update(IntSize(screenWidth, screenHeight))
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val canvas = drawContext.canvas
        for (snow in snowState.snows) {
            snow.draw(canvas)
        }
    }
}
