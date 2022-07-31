package com.hrb.holidays.ui.modifiers

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

enum class FrameLine {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM
}

fun DrawScope.drawFrameLines(vararg lines: FrameLine, color: Color, alpha: Float = 0.5f) {
    lines.forEach {
        val startX = if (it == FrameLine.RIGHT) size.width else 0f
        val startY = if (it == FrameLine.BOTTOM) size.height else 0f
        val endX = if (it == FrameLine.LEFT) 0f else size.width
        val endY = if (it == FrameLine.TOP) 0f else size.height

        drawLine(
            color,
            Offset(startX, startY),
            Offset(endX, endY),
            1f,
            alpha = alpha
        )
    }
}

@Composable
fun Modifier.frame(
    vararg drawLines: FrameLine,
    color: Color = MaterialTheme.colors.onSurface,
    alpha: Float = .5f
): Modifier = this.then(
    drawBehind {
        drawFrameLines(
            lines = drawLines,
            color = color,
            alpha = alpha
        )
    }
)
