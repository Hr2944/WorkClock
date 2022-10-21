package com.hrb.holidays.ui.components.datepicker.fullscreen

import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
internal fun Modifier.circleDay(
    daySize: Dp,
    dayPadding: Dp,
    dayCenterPositionProvider: () -> Offset?,
    color: Color = LocalContentColor.current,
    alpha: Float = 1f,
    style: DrawStyle = Stroke(1f),
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
    borderSize: Dp = 0.dp
): Modifier = this.then(
    CircleDayModifier(
        daySize = daySize,
        dayPadding = dayPadding,
        dayCenterPositionProvider = dayCenterPositionProvider,
        color = color,
        alpha = alpha,
        style = style,
        colorFilter = colorFilter,
        blendMode = blendMode,
        borderSize = borderSize
    )
)

private data class CircleDayModifier(
    private val daySize: Dp,
    private val dayPadding: Dp,
    private val dayCenterPositionProvider: () -> Offset?,
    private val color: Color,
    private val alpha: Float,
    private val style: DrawStyle,
    private val colorFilter: ColorFilter?,
    private val blendMode: BlendMode,
    private val borderSize: Dp
) : DrawModifier {
    override fun ContentDrawScope.draw() {
        val radius = (daySize - 2 * dayPadding - borderSize).toPx() / 2f
        val dayCenterPosition = dayCenterPositionProvider()

        drawContent()

        if (dayCenterPosition != null) {
            drawCircle(
                color = color,
                radius = radius,
                center = dayCenterPosition,
                alpha = alpha,
                style = style,
                colorFilter = colorFilter,
                blendMode = blendMode
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CircleDayModifier

        if (daySize != other.daySize) return false
        if (dayPadding != other.dayPadding) return false
        if (dayCenterPositionProvider != other.dayCenterPositionProvider) return false
        if (color != other.color) return false
        if (alpha != other.alpha) return false
        if (style != other.style) return false
        if (colorFilter != other.colorFilter) return false
        if (blendMode != other.blendMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = daySize.hashCode()
        result = 31 * result + dayPadding.hashCode()
        result = 31 * result + dayCenterPositionProvider.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + style.hashCode()
        result = 31 * result + (colorFilter?.hashCode() ?: 0)
        result = 31 * result + blendMode.hashCode()
        return result
    }
}
