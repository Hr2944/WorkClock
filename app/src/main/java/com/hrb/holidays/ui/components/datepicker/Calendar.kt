package com.hrb.holidays.ui.components.datepicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hrb.holidays.commons.ImmutableLocalDate
import com.hrb.holidays.commons.ImmutableYearMonth
import com.hrb.holidays.commons.capitalizeFirstChar
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil

@Immutable
internal data class CalendarDay(
    val date: ImmutableLocalDate,
    val isFirstWeekDay: Boolean,
    val isLastWeekDay: Boolean,
)

@Immutable
internal data class CalendarWeek(
    val days: ImmutableList<CalendarDay>,
    val firstDayIndex: Int,
    val lastDayIndex: Int,
    val totalSize: Int
)

@Immutable
internal data class CalendarMonth(
    val weeks: ImmutableList<CalendarWeek>, val month: ImmutableYearMonth, val id: Int
)

@Stable
internal class CalendarState(
    initialMonth: YearMonth = YearMonth.now(),
    initialCalendarMonths: List<CalendarMonth> = listOf(),
    private val daysOfWeek: List<DayOfWeek>,
) {
    private val idGenerator = AtomicInteger()
    val months: SnapshotStateList<CalendarMonth>

    init {
        months = mutableStateListOf(
            CalendarMonth(
                weeks = getMonthWeeks(
                    month = initialMonth,
                    startIndex = lookupStartIndex(previousMonth = initialMonth)
                ), month = ImmutableYearMonth(initialMonth), id = nextId()
            )
        )
        months.addAll(initialCalendarMonths)
    }

    fun loadNext() {
        val last = months.last()
        val nextMonth = last.month().plusMonths(1)
        months.add(
            CalendarMonth(
                weeks = getMonthWeeks(
                    month = nextMonth, startIndex = getNextStartIndex(currentMonth = last)
                ), month = ImmutableYearMonth(nextMonth), id = nextId()
            )
        )
    }

    fun loadPrevious() {
        val first = months.first()
        val previousMonth = first.month().minusMonths(1)
        months.add(
            0, CalendarMonth(
                weeks = getMonthWeeks(
                    month = previousMonth,
                    startIndex = lookupStartIndex(previousMonth = previousMonth)
                ), month = ImmutableYearMonth(previousMonth), id = nextId()
            )
        )
    }

    private fun nextId(): Int = idGenerator.addAndGet(1)

    private fun getNextStartIndex(currentMonth: CalendarMonth): Int {
        val index = currentMonth.weeks.last().lastDayIndex + 1
        return if (index < daysOfWeek.size) {
            index
        } else {
            0
        }
    }

    private fun lookupStartIndex(previousMonth: YearMonth): Int {
        val firstDay = previousMonth.atDay(1).dayOfWeek
        return daysOfWeek.indexOfFirst { it == firstDay }
    }

    private fun getMonthWeeks(
        month: YearMonth,
        startIndex: Int,
    ): ImmutableList<CalendarWeek> {
        val weeks = mutableListOf<CalendarWeek>()
        val nbWeeks = ceil(
            (month.lengthOfMonth() + startIndex) / daysOfWeek.size.toDouble()
        ).toInt()
        val daysOfWeekSizeMinusStartIndex = daysOfWeek.size - startIndex
        val daysOfWeekSizeMinusOne = daysOfWeek.size - 1

        var lastAddedDayIndex = 0

        weeks.add(
            CalendarWeek(
                days = List(daysOfWeekSizeMinusStartIndex) { i ->
                    lastAddedDayIndex++
                    CalendarDay(
                        date = ImmutableLocalDate(month.atDay(lastAddedDayIndex)),
                        isFirstWeekDay = i == 0,
                        isLastWeekDay = i == daysOfWeekSizeMinusStartIndex - 1
                    )
                }.toImmutableList(),
                firstDayIndex = startIndex,
                lastDayIndex = daysOfWeekSizeMinusOne,
                totalSize = daysOfWeek.size
            )
        )

        for (weekIndex in 1 until nbWeeks - 1) {
            weeks.add(
                CalendarWeek(
                    days = List(daysOfWeek.size) { j ->
                        lastAddedDayIndex++
                        CalendarDay(
                            date = ImmutableLocalDate(month.atDay(lastAddedDayIndex)),
                            isFirstWeekDay = j == 0,
                            isLastWeekDay = j == daysOfWeekSizeMinusOne
                        )
                    }.toImmutableList(),
                    firstDayIndex = 0,
                    lastDayIndex = daysOfWeekSizeMinusOne,
                    totalSize = daysOfWeek.size
                )
            )
        }

        val lastWeekSize = month.lengthOfMonth() - lastAddedDayIndex
        weeks.add(
            CalendarWeek(
                days = List(lastWeekSize) { j ->
                    lastAddedDayIndex++
                    CalendarDay(
                        date = ImmutableLocalDate(month.atDay(lastAddedDayIndex)),
                        isFirstWeekDay = j == 0,
                        isLastWeekDay = j == lastWeekSize - 1
                    )
                }.toImmutableList(),
                firstDayIndex = 0,
                lastDayIndex = lastWeekSize - 1,
                totalSize = daysOfWeek.size
            )
        )

        return weeks.toImmutableList()
    }

    companion object {
        fun Saver(): Saver<CalendarState, *> = Saver(save = {
            mapOf(
                "initialCalendarMonths" to it.months, "daysOfWeek" to it.daysOfWeek
            )
        }, restore = {
            CalendarState(
                initialCalendarMonths = it["initialCalendarMonths"] as List<CalendarMonth>,
                daysOfWeek = it["daysOfWeek"] as List<DayOfWeek>
            )
        })
    }
}

@Composable
internal fun rememberCalendarState(
    initialMonth: YearMonth = YearMonth.now(), daysOfWeek: List<DayOfWeek> = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )
): CalendarState {
    return rememberSaveable(saver = CalendarState.Saver()) {
        CalendarState(
            initialMonth = initialMonth, daysOfWeek = daysOfWeek
        )
    }
}

@Composable
internal fun Calendar(
    days: ImmutableList<CalendarWeek>,
    month: ImmutableYearMonth,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 12.dp,
    daySize: Dp = 48.dp,
    dayContent: @Composable RowScope.(Int, CalendarDay) -> Unit,
) {
    MonthHeader(month = month)

    days.forEach { (weekDays, firstDayIndex, lastDayIndex, totalSize) ->
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .padding(
                    start = daySize * firstDayIndex,
                    end = daySize * (totalSize - (lastDayIndex + 1))
                ),
        ) {
            weekDays.forEachIndexed { index, day ->
                key(index, day) {
                    dayContent(index, day)
                }
            }
        }
    }
}

@Composable
internal fun Day(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    onSelect: () -> Unit,
    day: ImmutableLocalDate,
    size: Dp = 48.dp,
    padding: Dp = 4.dp,
    isTodayProvider: () -> Boolean,
    isSelectedProvider: () -> Boolean
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .clickable(onClick = onSelect,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .padding(padding)
            .then(modifier)
    ) {
        DayText(
            day = day,
            backgroundColor = backgroundColor,
            isTodayProvider = isTodayProvider,
            isSelectedProvider = isSelectedProvider
        )
    }
}

@Composable
private fun DayText(
    day: ImmutableLocalDate,
    backgroundColor: Color,
    isTodayProvider: () -> Boolean,
    isSelectedProvider: () -> Boolean
) {
    val dayFormatter = remember { DateTimeFormatter.ofPattern("d") }
    val text = remember(day) {
        dayFormatter.format(day())
    }
    val color = contentColorFor(
        if (isSelectedProvider()) backgroundColor
        else Color.Unspecified
    )

    Text(text = text,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        color = color,
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                if (isTodayProvider()) {
                    drawCircle(
                        color = color,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
            }
            .wrapContentHeight())
}

@Composable
internal fun WeekDaysHeader(dayTags: ImmutableList<String>) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            dayTags.forEach {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)
                ) {
                    Text(text = it, style = MaterialTheme.typography.body2)
                }

            }
        }
    }
    Divider(modifier = Modifier.fillMaxWidth())
}

@Composable
internal fun MonthHeader(
    month: ImmutableYearMonth
) {
    val monthFormatter = remember {
        DateTimeFormatter.ofPattern("MMMM yyyy")
    }
    val text = remember(month) {
        month().format(monthFormatter).capitalizeFirstChar()
    }

    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(start = 12.dp)
                .paddingFromBaseline(top = 32.dp, bottom = 16.dp)
        )
    }
}
