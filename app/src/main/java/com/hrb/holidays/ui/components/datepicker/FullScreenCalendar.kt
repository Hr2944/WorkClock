package com.hrb.holidays.ui.components.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
internal fun FullScreenCalendar(
    onSelectDate: (LocalDate) -> Unit,
    daysOfWeek: ImmutableList<DayOfWeek>,
    pickerState: DateRangePickerState
) {
    val monthsState = rememberCalendarState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    lazyListState.OnTopOrBottomReached {
        when (it) {
            Reached.Top -> {
                scope.launch {
                    monthsState.loadPrevious()
                }
            }
            Reached.Bottom -> {
                scope.launch {
                    monthsState.loadNext()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { monthsState.loadNext() }) {
            Text(text = "Add")
        }
        WeekDaysHeader(
            dayTags = remember {
                daysOfWeek.map {
                    it.getDisplayName(
                        TextStyle.NARROW,
                        Locale.getDefault()
                    )
                }.toImmutableList()
            }
        )
        CalendarBody(
            onSelectDate = onSelectDate,
            calendarState = monthsState,
            pickerState = pickerState,
            lazyListState = lazyListState
        )
    }
}

@Composable
private fun CalendarBody(
    onSelectDate: (LocalDate) -> Unit,
    lazyListState: LazyListState,
    calendarState: CalendarState,
    pickerState: DateRangePickerState
) {
    val inRangeColor = MaterialTheme.colors.primarySurface.copy(alpha = .13f)
    val backgroundColor = MaterialTheme.colors.primarySurface

    LazyColumn(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
        items(
            calendarState.months,
            key = { it.id }
        ) { (weeks, month, _) ->
            Calendar(
                days = weeks,
                month = month
            ) { _, day ->
                val isSelected by remember {
                    derivedStateOf {
                        pickerState.isSelected(day.date())
                    }
                }
                val isToday by remember {
                    derivedStateOf {
                        pickerState.isToday(day.date())
                    }
                }
                Day(
                    modifier = Modifier
                        .dateRangeSelection(
                            paddingProvider = { 4.dp },
                            inRangeColorProvider = { inRangeColor },
                            backgroundColorProvider = { backgroundColor },
                            isFirstWeekDayProvider = { day.isFirstWeekDay },
                            isLastWeekDayProvider = { day.isLastWeekDay },
                            isInExclusiveRangeProvider = { pickerState.isInExclusiveRange(day.date()) },
                            endDateProvider = { pickerState.endDate },
                            isStartDateProvider = { pickerState.isStartDate(day.date()) },
                            isEndDateProvider = { pickerState.isEndDate(day.date()) }
                        ),
                    day = day.date,
                    onSelect = { onSelectDate(day.date()) },
                    isTodayProvider = { isToday },
                    isSelectedProvider = { isSelected }
                )
            }
        }
    }
}

private fun Modifier.dateRangeSelection(
    paddingProvider: () -> Dp,
    inRangeColorProvider: () -> Color,
    backgroundColorProvider: () -> Color,
    isFirstWeekDayProvider: () -> Boolean,
    isLastWeekDayProvider: () -> Boolean,
    isInExclusiveRangeProvider: () -> Boolean,
    endDateProvider: () -> LocalDate?,
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

private class DateRangeSelection(
    private val paddingProvider: () -> Dp,
    private val inRangeColorProvider: () -> Color,
    private val backgroundColorProvider: () -> Color,
    private val isFirstWeekDayProvider: () -> Boolean,
    private val isLastWeekDayProvider: () -> Boolean,
    private val isInExclusiveRangeProvider: () -> Boolean,
    private val endDateProvider: () -> LocalDate?,
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
