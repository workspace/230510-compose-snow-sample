package com.github.workspace.snowsample.ui.effects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.github.workspace.snowsample.random

fun createParticle(startOffset: Offset, screenSize: IntSize): Particle {

    return Particle(
        color = Color(listOf(0xffea4335, 0xff4285f4, 0xfffbbc05, 0xff34a853).random()),
        startXPosition = startOffset.x.toInt(),
        startYPosition = startOffset.y.toInt(),

        // 최대 수평 변위
        maxHorizontalDisplacement = screenSize.width * (-0.9f..0.9f).random(),
        // 최대 높이(최대 수직 변위)
        maxVerticalDisplacement = screenSize.height * (0.2f..1f).random(),
    )
}