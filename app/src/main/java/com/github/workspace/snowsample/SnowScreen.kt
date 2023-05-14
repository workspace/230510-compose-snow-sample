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
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class SnowState(
    val snows: List<Snow>,
)

class Snow(
    val size: Float,
    val screenSize: IntSize,
    position: Offset,
    angle: Float,
) {
    var rotation = 0f
    private var rotationWidth = size
    val rotationSpeed3D: Float = 1f
    val rotationSpeed2D: Float = 1f
    var scaleX = 0f

    private var speedF = 60f

    val paint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.White
        style = PaintingStyle.Fill
    }
    var position by mutableStateOf(position)
    private var angle by mutableStateOf(angle)
    private var increment = incrementRange
    private val duration = 2F // 초
    private val velocityXMax = (screenSize.width / 2) / duration
    val velocityX = (-velocityXMax..velocityXMax).random()
    private var velocity = (-4F) * screenSize.height / duration
    private val acc = 8F * screenSize.height / duration / duration
    fun draw(canvas: Canvas) {
        canvas.drawCircle(position, size, paint)
    }

    var isReachHeight: Boolean = false
    fun update(deltaTime: Float) {
        val xAngle = increment * cos(angle)
        val yAngle = increment * sin(angle)

        rotation += rotationSpeed2D * deltaTime * speedF
        if (rotation >= 360) rotation = 0f

        // 스케일을 주면서 회전한듯한 이펙트를 심어준다.
        rotationWidth -= abs(rotationSpeed3D) * deltaTime * speedF
        if (rotationWidth < 0) {
            rotationWidth = size
        }

        scaleX = abs(rotationWidth / size - 0.5f) * 2

        position = position.copy(
            x = position.x + velocityX * deltaTime,
            y = position.y + velocity * deltaTime
        )
        velocity += acc * deltaTime
    }
}

private const val angleSeed = 25F
private val angleSeedRange = -angleSeed..angleSeed
private val incrementRange = 10.0f

private val maxHeight = 500.0f

fun createSnowList(canvas: IntSize): List<Snow> {
    return List(20) {
        Snow(
            size = 20F,
            canvas,
            position = Offset(
                x = canvas.width.toFloat() / 2,
                y = canvas.height.toFloat(),
            ),
            angle = angleSeedRange.random() / angleSeed / 0.001f + (PI.toFloat()),
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

    val frameTime = remember { mutableStateOf(0L) }

    var snowState by remember {
        mutableStateOf(
            SnowState(
                createSnowList(
                    IntSize(screenWidth, screenHeight),
                ),
            ),
        )
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameMillis { frameMs ->
                val deltaMs = if (frameTime.value > 0) (frameMs - frameTime.value) else 0
                frameTime.value = frameMs

                for (snow in snowState.snows) {
                    snow.update(deltaMs.div(1000f))
                }
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        val canvas = drawContext.canvas

        snowState.snows.forEach {
            withTransform(
                transformBlock = {
                    rotate(
                        degrees = it.rotation,
                        pivot = Offset(
                            x = it.position.x + it.size / 2,
                            y = it.position.y + it.size / 2,
                        ),
                    )
                    scale(
                        scaleX = it.scaleX,
                        scaleY = 1f,
                        pivot = Offset(
                            x = it.position.x + it.size / 2,
                            y = it.position.y,
                        ),
                    )
                },
            ) {
                it.draw(canvas)
            }
        }
    }
}
