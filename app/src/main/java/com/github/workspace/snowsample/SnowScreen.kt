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
import kotlin.random.Random

data class SnowState(
    val snows: List<Snow>
)

class Snow(
    val size: Float,
    position: Offset,
    val screenSize: IntSize
) {
    val paint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.White
        style = PaintingStyle.Fill
    }
    private var position by mutableStateOf(position)

    fun draw(canvas: Canvas) {
        canvas.drawCircle(position, size, paint)
    }

    fun update() {
        position = if (position.y > screenSize.height) {
            position.copy(y = 0F)
        } else {
            position.copy(y = position.y + 30F)
        }
    }
}

fun createSnowList(canvas: IntSize): List<Snow> {
    return List(10) {
        Snow(
            size = 20F,
            position = Offset(x = canvas.width.randomTest().toFloat(), y = 20F),
            canvas
        )
    }
}

fun Int.randomTest() = Random.nextInt(this)

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
        while(isActive) {
            awaitFrame()
            for (snow in snowState.snows) {
                snow.update()
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
