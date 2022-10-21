package com.hrb.holidays.ui.components.datepicker.fullscreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
internal fun CanvasMonth(
    cMonth: CalendarMonth,
    textFontSizeInPx: Float,
    textColorInArgb: Int,
    dayBoundingBoxesState: DayBoundingBoxesState,
    daySize: Dp = 48.dp,
    dayPadding: Dp = 4.dp,
    horizontalPadding: Dp = 12.dp,
    modifier: Modifier = Modifier
) {
    MonthName(month = cMonth.month)

    Layout(
        measurePolicy = { _, constraints ->
            layout(constraints.minWidth, constraints.minHeight) {}
        },
        content = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(cMonth.weeks.size * (2 * dayPadding + daySize))
            .padding(horizontalPadding)
            .pointerInput(Unit) {
                detectTapGestures {
                    dayBoundingBoxesState.click(it)
                }
            }
            .drawWithCache {
                val dayPxSize = (daySize - 2 * dayPadding).toPx()
                val dayPxPadding = dayPadding.toPx()
                val initialLeftPadding = (daySize * cMonth.firstDayIndex).toPx() + dayPxPadding
                var currentX = initialLeftPadding
                var currentY = dayPxPadding

                onDrawWithContent {
                    drawIntoCanvas { canvas ->
                        cMonth.fastForEachDay { (date, _, _) ->
                            val dayText = date
                                .getDayOfMonth()
                                .toString()
                            val layout = DayTextLayoutCache.getOrCreate(
                                dayText,
                                textFontSizeInPx
                            )
                            layout.painter.apply {
                                color = textColorInArgb
                            }

                            canvas.nativeCanvas.drawText(
                                dayText,
                                (currentX + dayPxSize / 2f) - layout.rectWidth / 2f - layout.rectLeft,
                                (currentY + dayPxSize / 2f) + layout.rectHeight / 2f - layout.rectBottom,
                                layout.painter
                            )

                            dayBoundingBoxesState.addDay(
                                Rect(
                                    left = currentX - 2 * dayPxPadding,
                                    top = currentY - 2 * dayPxPadding,
                                    right = currentX + dayPxSize + 2 * dayPxPadding,
                                    bottom = currentY + dayPxSize + 2 * dayPxPadding
                                )
                            )

                            currentX += dayPxSize + dayPxPadding

                            if (currentX + dayPxSize + dayPxPadding >= this.size.width) {
                                currentY += dayPxSize + (2 * dayPxPadding)
                                currentX = 0f
                            }

                            currentX += dayPxPadding
                        }

                        currentX = initialLeftPadding
                        currentY = dayPxPadding
                    }

                    drawContent()
                }
            }
            .then(modifier)
    )
}
