package com.github.workspace.snowsample.ui.polygon

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import java.util.LinkedList
import java.util.Queue
import kotlin.math.sqrt

data class Polygon(
    val vectors: List<Pair<Float, Float>>
) {

    fun makePath(matrix: Matrix): Path {

        val queue: Queue<Pair<Float, Float>> = LinkedList<Pair<Float, Float>>().apply {
            vectors.map {
                matrix.map(Offset(it.first, it.second))
            }.apply {
                addAll(this.map {
                    Pair(it.x, it.y)
                })
            }
        }

        val path = Path()
        var cur: Pair<Float, Float> = queue.poll() ?: return path
        path.moveTo(cur.first, cur.second)
        while (queue.isNotEmpty()) {
            cur = queue.poll() ?: break
            path.lineTo(cur.first, cur.second)
        }
        path.close()
        return path
    }

    companion object {

        fun makeTriangle(edge: Float): Polygon {
            val height = edge * sqrt(3f) / 2f
            val heightAbove = height / 3 * 2
            val heightBelow = height / 3
            return Polygon(
                listOf(
                    Pair(0f, -heightAbove),
                    Pair(-edge / 2, heightBelow),
                    Pair(edge / 2, heightBelow),
                )
            )
        }
    }
}