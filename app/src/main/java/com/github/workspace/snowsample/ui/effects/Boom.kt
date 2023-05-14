package com.github.workspace.snowsample.ui.effects

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.workspace.snowsample.random
import java.util.Random
import kotlin.math.pow

@Composable
fun Boom() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var progress by remember { mutableStateOf(0f) }

        Boom(progress)

        Slider(
            modifier = Modifier.width(200.dp),
            value = progress,
            onValueChange = {
                progress = it
            },
        )
    }
}

@Composable
fun Boom(progress: Float) {
    val sizeDp = 200.dp
    val sizePx = sizeDp.toPx()
    val sizePxHalf = sizePx / 2
    val particles = remember {
        List(150) {
            Particle(
                color = Color(listOf(0xffea4335, 0xff4285f4, 0xfffbbc05, 0xff34a853).random()),
                startXPosition = sizePxHalf.toInt(),
                startYPosition = sizePxHalf.toInt(),

                // 최대 수평 변위
                maxHorizontalDisplacement = sizePx * (-0.9f..0.9f).random(),
                // 최대 높이(최대 수직 변위)
                maxVerticalDisplacement = sizePx * (0.2f..3f).random(),
            )
        }
    }
    particles.forEach { it.updateProgress(progress) }

    Canvas(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0x26000000))
            .size(sizeDp),
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(sizePxHalf, 0f),
            end = Offset(sizePxHalf, sizePx),
            strokeWidth = 2.dp.toPx(),
        )
        drawLine(
            color = Color.Black,
            start = Offset(0f, sizePxHalf),
            end = Offset(sizePx, sizePxHalf),
            strokeWidth = 2.dp.toPx(),
        )
        particles.forEach { particle ->
            drawCircle(
                alpha = particle.alpha,
                color = particle.color,
                radius = particle.currentRadius,
                center = Offset(particle.currentXPosition, particle.currentYPosition),
            )
        }
    }
}

class Particle(
    val color: Color,
    val startXPosition: Int,
    val startYPosition: Int,
    //  최고 수평 변위
    val maxHorizontalDisplacement: Float,
    // 최고 수직 변위
    val maxVerticalDisplacement: Float,
) {
    // 도달할 수 있는 최대 높이를 기준으로 입자의 속도와 가속도를 결정해야 한다.
    // 속도 계산 -> 4 * 수직 변위(최고 높이)
    val velocity = 4 * maxVerticalDisplacement

    // 가속도 공식 -> -2 * 속도
    val acceleration = -2 * velocity

    var currentXPosition = 0f
    var currentYPosition = 0f

    // 입자가 모두 동시에 움직이기 시작하는 것이 아니라 분사되는 것처럼 나타나도록 하여 초기 애니메이션을 개선
    var visibilityThresholdLow = (0f..0.14f).random()

    var visibilityThresholdHigh = (0f..0.4f).random()

    // 처음 시작했을때 한곳에 뭉쳐있지 않게
    val initialXDisplacement = 10.dp.toPx() * (-1f..1f).random()
    val initialYDisplacement = 10.dp.toPx() * (-1f..1f).random()

    var alpha = 0f
    var currentRadius = 0f
    val startRadius = 2.dp.toPx()

    // 20% 정도만 점진적으로 확장
    val endRadius = if (randomBoolean(trueProbabilityPercentage = 20)) {
        (startRadius..7.dp.toPx()).random()
    } else {
        (1.5.dp.toPx()..startRadius).random()
    }

    fun updateProgress(explosionProgress: Float) {
        // 궤적 진행
        val trajectoryProgress =
            // visibilityThresholdLow 보다 적거나 , visibilityThresholdHigh보다
            if (explosionProgress < visibilityThresholdLow || (explosionProgress > (1 - visibilityThresholdHigh))) {
                alpha = 0f
                return
            } else {
                (explosionProgress - visibilityThresholdLow).mapInRange(
                    0f,
                    1f - visibilityThresholdHigh - visibilityThresholdLow,
                    0f,
                    1f,
                )
            }
        alpha = if (trajectoryProgress < 0.7f) {
            1f
        } else {
            (trajectoryProgress - 0.7f).mapInRange(
                0f,
                0.3f,
                0.7f,
                0f,
            )
        }

        currentRadius = startRadius + (endRadius - startRadius) * trajectoryProgress

        val currentTime = trajectoryProgress.mapInRange(0f, 1f, 0f, 1.4f)

        // = ut + 0.5at^2

        val verticalDisplacement = (
                currentTime * velocity + 0.5 * acceleration * currentTime.toDouble()
                    .pow(2.0)
                ).toFloat()
        currentXPosition =

            startXPosition + initialYDisplacement + maxHorizontalDisplacement * trajectoryProgress

        //하늘로 올라가려면 뺴야함
        currentYPosition = startYPosition + initialXDisplacement - verticalDisplacement
    }
}

//
fun Float.mapInRange(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return outMin + (((this - inMin) / (inMax - inMin)) * (outMax - outMin))
}

fun Dp.toPx() = value.dpToPx()

fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density

private val random = Random()

fun randomBoolean(trueProbabilityPercentage: Int) =
    random.nextFloat() < trueProbabilityPercentage / 100f

@Preview
@Composable
fun BoomPreview() {
    Boom()
}
