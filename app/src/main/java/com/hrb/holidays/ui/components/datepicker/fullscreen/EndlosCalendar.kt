package com.hrb.holidays.ui.components.datepicker.fullscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.hrb.holidays.commons.immutable.ImmutableLocalDate
import com.hrb.holidays.ui.components.datepicker.CalendarTypography
import com.hrb.holidays.ui.components.datepicker.RangePickerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
internal fun EndlosCalendar(
    onSelectDate: (ImmutableLocalDate) -> Unit,
    daysOfWeek: ImmutableList<DayOfWeek>,
    pickerState: RangePickerState
) {
    val calendarState = rememberCalendarState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    lazyListState.OnTopOrBottomReached {
        when (it) {
            Reached.Top -> {
                scope.launch {
                    calendarState.addPreviousMonth()
                }
            }
            Reached.Bottom -> {
                scope.launch {
                    calendarState.addNextMonth()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        WeekHeader(
            dayTags = remember {
                daysOfWeek.map {
                    it.getDisplayName(
                        TextStyle.NARROW,
                        Locale.getDefault()
                    )
                }.toImmutableList()
            }
        )
        LazyCalendar(
            onSelectDate = onSelectDate,
            calendarState = calendarState,
            pickerState = pickerState,
            lazyListState = lazyListState
        )
    }
}

@Composable
private fun LazyCalendar(
    onSelectDate: (ImmutableLocalDate) -> Unit,
    lazyListState: LazyListState,
    calendarState: CalendarState,
    pickerState: RangePickerState
) {
    val inRangeColor = MaterialTheme.colors.primarySurface.copy(alpha = .13f)
    val backgroundColor = MaterialTheme.colors.primary
    val contentColor = LocalContentColor.current
    val textSelectedColorInArgb = contentColorFor(MaterialTheme.colors.primary).toArgb()
    val textColorInArgb = contentColorFor(Color.Unspecified).toArgb()
    val todayBorderSizeInPx: Float
    val textFontSizeInPx: Float
    with(LocalDensity.current) {
        todayBorderSizeInPx = remember { 1.dp.toPx() }
        textFontSizeInPx = CalendarTypography.day.fontSize.toPx()
    }
    val todayStyle = remember { Stroke(width = todayBorderSizeInPx) }

    Column {
        val scope = rememberCoroutineScope()
        val lazyListState2 = rememberLazyListState()
        lazyListState2.OnTopOrBottomReached {
            when (it) {
                Reached.Top -> {
                    scope.launch {
                        calendarState.addPreviousMonth()
                    }
                }
                Reached.Bottom -> {
                    scope.launch {
                        calendarState.addNextMonth()
                    }
                }
            }
        }

        LazyColumn(
            state = lazyListState2,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true)
        ) {
            items(
                calendarState.months,
                key = { it.id }
            ) { month ->
                val dayBoundingBoxesState = rememberDayBoundingBoxesState(month) { dayIndex ->
                    month.findDayIndexed { i -> i == dayIndex }?.let {
                        onSelectDate(it.date)
                    }
                }

                CanvasMonth(
                    cMonth = month,
                    textFontSizeInPx = textFontSizeInPx,
                    textColorInArgb = textColorInArgb,
                    dayBoundingBoxesState = dayBoundingBoxesState,
                    modifier = Modifier
                        .circleDay(
                            daySize = 48.dp,
                            dayPadding = 4.dp,
                            style = todayStyle,
                            borderSize = 1.dp,
                            dayCenterPositionProvider = {
                                val i = month.fastFindFirstDayIndex { _, (date, _, _) ->
                                    date == pickerState.today
                                }
                                i?.let { dayBoundingBoxesState.getBoxOrNull(it)?.center }
                            }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true)
        ) {
            items(
                calendarState.months,
                key = { it.id }
            ) { month ->
                Month(
                    month = month,
                    onSelectDay = { _, (date, _, _) -> onSelectDate(date) },
                ) { (date, isFirstWeekDay, isLastWeekDay) ->
                    val isToday by remember {
                        derivedStateOf { pickerState.isToday(date) }
                    }
                    val isSelected by remember {
                        derivedStateOf { pickerState.isSelected(date) }
                    }
                    val isInExclusiveRange by remember {
                        derivedStateOf { pickerState.isInExclusiveRange(date) }
                    }
                    val isStartDate by remember {
                        derivedStateOf { pickerState.isStartDate(date) }
                    }
                    val isEndDate by remember {
                        derivedStateOf { pickerState.isEndDate(date) }
                    }
                    Day(
                        modifier = Modifier
                            .rangeSelection(
                                paddingProvider = { 4.dp },
                                inRangeColorProvider = { inRangeColor },
                                backgroundColorProvider = { backgroundColor },
                                isFirstWeekDayProvider = { isFirstWeekDay },
                                isLastWeekDayProvider = { isLastWeekDay },
                                isInExclusiveRangeProvider = { isInExclusiveRange },
                                endDateProvider = { pickerState.endDate },
                                isStartDateProvider = { isStartDate },
                                isEndDateProvider = { isEndDate }
                            ),
                        day = date,
                        todayBorderColorProvider = { contentColor },
                        isTodayProvider = { isToday },
                        isSelectedProvider = { isSelected },
                        todayBorderSizeInPx = todayBorderSizeInPx,
                        textFontSizeInPx = textFontSizeInPx,
                        textSelectedColorInArgb = textSelectedColorInArgb,
                        textColorInArgb = textColorInArgb,
                        todayStyle = todayStyle
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekHeader(dayTags: ImmutableList<String>) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            dayTags.forEach {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
                    Text(text = it, style = CalendarTypography.week)
                }

            }
        }
    }
    Divider(modifier = Modifier.fillMaxWidth())
}
