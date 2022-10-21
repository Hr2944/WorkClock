package com.hrb.holidays.ui.components.datepicker.fullscreen

import android.graphics.Rect
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import com.hrb.holidays.commons.immutable.ImmutableLocalDate
import android.graphics.Paint as NativePaint


@Composable
internal fun Day(
    modifier: Modifier = Modifier,
    day: ImmutableLocalDate,
    size: Dp = 48.dp,
    padding: Dp = 4.dp,
    todayBorderColorProvider: () -> Color,
    isTodayProvider: () -> Boolean,
    isSelectedProvider: () -> Boolean,
    todayBorderSizeInPx: Float,
    textFontSizeInPx: Float,
    textSelectedColorInArgb: Int,
    textColorInArgb: Int,
    todayStyle: DrawStyle
) {
    val text = remember(day) {
        day.getDayOfMonth().toString()
    }

    val textLayout = remember(text) {
        DayTextLayoutCache.getOrCreate(dayText = text, textFontSizeInPx = textFontSizeInPx)
    }

    Layout(
        content = {},
        measurePolicy = DayTextMeasurePolicy,
        modifier = Modifier
            .size(size)
            .padding(padding)
            .drawWithCache {
                textLayout.painter.color =
                    if (isSelectedProvider()) textSelectedColorInArgb
                    else textColorInArgb
                val todayColor = todayBorderColorProvider()
                val isToday = isTodayProvider()

                onDrawWithContent {
                    if (isToday) {
                        drawCircle(
                            color = todayColor,
                            style = todayStyle,
                            radius = (this.size.minDimension - todayBorderSizeInPx) / 2f
                        )
                    }
                    drawContent()
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            text,
                            this.size.width / 2f - textLayout.rectWidth / 2f - textLayout.rectLeft,
                            this.size.height / 2f + textLayout.rectHeight / 2f - textLayout.rectBottom,
                            textLayout.painter
                        )
                    }
                }
            }
            .then(modifier)
    )
}

private val DayTextMeasurePolicy = MeasurePolicy { _, constraints ->
    layout(constraints.minWidth, constraints.minHeight) {}
}

private object DayTextMeasurePolicyCache {
    private val policies = mutableMapOf<Dp, MeasurePolicy>()

    fun get(size: Dp, padding: Dp): MeasurePolicy {
        return policies.getOrPut(size) {
            create(size, padding)
        }
    }

    // TODO: make it works, and create a cache for the MeasurePolicy as it is called on every days
    private fun create(size: Dp, padding: Dp): MeasurePolicy {
        return MeasurePolicy { measurables, constraints ->
            val roundedPadding = padding.roundToPx()
            val roundedSize = size.roundToPx()

            val horizontal = roundedPadding + roundedPadding
            val vertical = roundedPadding + roundedPadding

            val childConstraints = Constraints(
                minWidth = roundedSize,
                minHeight = roundedSize,
                maxWidth = roundedSize,
                maxHeight = roundedSize
            )

            val placeables = measurables.fastMap {
                it.measure(childConstraints.offset(-horizontal, -vertical))
            }

            val width = childConstraints.constrainWidth(roundedSize)
            val height = childConstraints.constrainHeight(roundedSize)
            layout(width, height) {
                placeables.fastForEach {
                    it.placeRelative(padding.roundToPx(), padding.roundToPx())
                }
            }
        }
    }
}

internal data class DayTextLayout(
    val painter: NativePaint,
    val rectWidth: Int,
    val rectLeft: Int,
    val rectHeight: Int,
    val rectBottom: Int
)

internal object DayTextLayoutCache {
    private val layouts: MutableMap<String, DayTextLayout> = mutableMapOf()

    fun getOrCreate(dayText: String, textFontSizeInPx: Float): DayTextLayout {
        return layouts.getOrPut(dayText) {
            create(
                dayText = dayText,
                textFontSizeInPx = textFontSizeInPx
            )
        }
    }

    private fun create(dayText: String, textFontSizeInPx: Float): DayTextLayout {
        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textAlign = NativePaint.Align.LEFT
            textSize = textFontSizeInPx
        }
        val r = Rect()
        textPaint.getTextBounds(dayText, 0, dayText.length, r)
        return DayTextLayout(
            painter = textPaint,
            rectBottom = r.bottom,
            rectWidth = r.width(),
            rectHeight = r.height(),
            rectLeft = r.left
        )
    }
}
