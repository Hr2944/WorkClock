package com.hrb.holidays.ui.components.datepicker.fullscreen

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import com.hrb.holidays.commons.immutable.ImmutableLocalDate


internal fun Modifier.rangeSelection(
    paddingProvider: () -> Dp,
    inRangeColorProvider: () -> Color,
    backgroundColorProvider: () -> Color,
    isFirstWeekDayProvider: () -> Boolean,
    isLastWeekDayProvider: () -> Boolean,
    isInExclusiveRangeProvider: () -> Boolean,
    endDateProvider: () -> ImmutableLocalDate?,
    isStartDateProvider: () -> Boolean,
    isEndDateProvider: () -> Boolean
) = this.then(
    DateRangeSelection(
        paddingProvider = paddingProvider,
        inRangeColorProvider = inRangeColorProvider,
        backgroundColorProvider = backgroundColorProvider,
        isFirstWeekDayProvider = isFirstWeekDayProvider,
        isLastWeekDayProvider = isLastWeekDayProvider,
        isInExclusiveRangeProvider = isInExclusiveRangeProvider,
        endDateProvider = endDateProvider,
        isStartDateProvider = isStartDateProvider,
        isEndDateProvider = isEndDateProvider
    )
)

internal class DateRangeSelection(
    private val paddingProvider: () -> Dp,
    private val inRangeColorProvider: () -> Color,
    private val backgroundColorProvider: () -> Color,
    private val isFirstWeekDayProvider: () -> Boolean,
    private val isLastWeekDayProvider: () -> Boolean,
    private val isInExclusiveRangeProvider: () -> Boolean,
    private val endDateProvider: () -> ImmutableLocalDate?,
    private val isStartDateProvider: () -> Boolean,
    private val isEndDateProvider: () -> Boolean
) : DrawModifier {

    override fun ContentDrawScope.draw() {
        if (isInExclusiveRangeProvider()) {
            if (isFirstWeekDayProvider()) {
                if (isLastWeekDayProvider()) {
                    drawArc(
                        color = inRangeColorProvider(),
                        startAngle = -90f,
                        sweepAngle = 180f,
                        useCenter = true
                    )
                } else {
                    drawRect(
                        color = inRangeColorProvider(),
                        size = Size((size.width / 2) + paddingProvider().toPx(), size.height),
                        topLeft = Offset(size.width / 2, 0f)
                    )
                }
                drawArc(
                    color = inRangeColorProvider(),
                    startAngle = 90f,
                    sweepAngle = 180f,
                    useCenter = true
                )
            } else if (isLastWeekDayProvider()) {
                drawRect(
                    color = inRangeColorProvider(),
                    size = Size((size.width / 2) + paddingProvider().toPx(), size.height),
                    topLeft = Offset(x = -paddingProvider().toPx(), y = 0f)
                )
                drawArc(
                    color = inRangeColorProvider(),
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = true
                )
            } else {
                drawRect(
                    color = inRangeColorProvider(),
                    size = Size(
                        width = size.width + 2 * paddingProvider().toPx(),
                        height = size.height
                    ),
                    topLeft = Offset(x = -paddingProvider().toPx(), y = 0f)
                )
            }

        } else {
            if (isStartDateProvider()) {
                if (!isLastWeekDayProvider() && endDateProvider() != null) {
                    drawRect(
                        color = inRangeColorProvider(),
                        topLeft = Offset(x = size.width / 2f, y = 0f),
                        size = Size(
                            width = (size.width / 2f) + paddingProvider().toPx(),
                            height = size.height
                        )
                    )
                }
                drawCircle(
                    color = backgroundColorProvider()
                )
            } else if (isEndDateProvider()) {
                if (!isFirstWeekDayProvider()) {
                    drawRect(
                        color = inRangeColorProvider(),
                        topLeft = Offset(x = -paddingProvider().toPx(), y = 0f),
                        size = Size(
                            width = (size.width / 2) + paddingProvider().toPx(),
                            height = size.height
                        )
                    )
                }
                drawCircle(
                    color = backgroundColorProvider()
                )
            }
        }

        drawContent()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateRangeSelection) return false

        if (paddingProvider != other.paddingProvider) return false
        if (inRangeColorProvider != other.inRangeColorProvider) return false
        if (backgroundColorProvider != other.backgroundColorProvider) return false
        if (isFirstWeekDayProvider != other.isFirstWeekDayProvider) return false
        if (isLastWeekDayProvider != other.isLastWeekDayProvider) return false
        if (isInExclusiveRangeProvider != other.isInExclusiveRangeProvider) return false
        if (endDateProvider != other.endDateProvider) return false
        if (isStartDateProvider != other.isStartDateProvider) return false
        if (isEndDateProvider != other.isEndDateProvider) return false

        return true
    }

    override fun hashCode(): Int {
        var result = paddingProvider.hashCode()
        result = 31 * result + inRangeColorProvider.hashCode()
        result = 31 * result + backgroundColorProvider.hashCode()
        result = 31 * result + isFirstWeekDayProvider.hashCode()
        result = 31 * result + isLastWeekDayProvider.hashCode()
        result = 31 * result + isInExclusiveRangeProvider.hashCode()
        result = 31 * result + endDateProvider.hashCode()
        result = 31 * result + isStartDateProvider.hashCode()
        result = 31 * result + isEndDateProvider.hashCode()
        return result
    }
}
