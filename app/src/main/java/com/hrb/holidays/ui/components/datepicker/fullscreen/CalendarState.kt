package com.hrb.holidays.ui.components.datepicker.fullscreen

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.hrb.holidays.commons.immutable.ImmutableLocalDate
import com.hrb.holidays.commons.immutable.ImmutableYearMonth
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil

@Immutable
@Parcelize
internal data class CalendarDay(
    val date: ImmutableLocalDate,
    val isFirstWeekDay: Boolean,
    val isLastWeekDay: Boolean,
) : Parcelable

@Immutable
@Parcelize
internal data class CalendarWeek(
    val days: List<CalendarDay>,
    val firstDayIndex: Int,
    val lastDayIndex: Int, val
    totalSize: Int
) : Parcelable

@Immutable
@Parcelize
internal data class CalendarMonth(
    val weeks: List<CalendarWeek>,
    val month: ImmutableYearMonth,
    val id: Int
) : Parcelable {
    val firstDayIndex: Int
        get() = weeks.first().firstDayIndex
}

internal inline fun CalendarMonth.fastForEachDay(action: (CalendarDay) -> Unit) {
    weeks.fastForEach {
        it.days.fastForEach(action)
    }
}

internal inline fun CalendarMonth.fastForEachDayIndexed(action: (Int, CalendarDay) -> Unit) {
    var i = 0
    weeks.fastForEach {
        it.days.fastForEach { day ->
            action(i, day)
            i++
        }
    }
}

internal inline fun CalendarMonth.fastFindFirstDayIndex(
    predicate: (Int, CalendarDay) -> Boolean
): Int? {
    var i = 0
    weeks.fastForEach { (days, _, _, _) ->
        days.fastForEachIndexed { _, day ->
            if (predicate(i, day)) return i
            i++
        }
    }
    return null
}

internal inline fun CalendarMonth.findDayIndexed(
    predicate: (Int) -> Boolean
): CalendarDay? {
    var i = 0
    for ((days, _, _, _) in this.weeks) for (day in days) {
        if (predicate(i)) return day
        i++
    }
    return null
}

@Stable
internal class CalendarState(
    initialMonth: ImmutableYearMonth = ImmutableYearMonth.now(),
    initialCalendarMonths: List<CalendarMonth> = listOf(),
    private val daysOfWeek: List<DayOfWeek>,
    private val maxSize: Int = 60
) {
    private val idGenerator = AtomicInteger()
    val months: SnapshotStateList<CalendarMonth>

    init {
        months = mutableStateListOf(
            CalendarMonth(
                weeks = getMonthWeeks(
                    month = initialMonth,
                    startIndex = lookupStartIndex(previousMonth = initialMonth)
                ),
                month = initialMonth,
                id = nextId()
            )
        )
        months.addAll(initialCalendarMonths)
    }

    fun addNextMonth() {
        if (months.size > maxSize) {
            months.removeFirst()
        }

        val last = months.last()
        val nextMonth = last.month.plusMonths(1)
        months.add(
            CalendarMonth(
                weeks = getMonthWeeks(
                    month = nextMonth,
                    startIndex = getNextStartIndex(currentMonth = last)
                ),
                month = nextMonth,
                id = nextId()
            )
        )
    }

    fun addPreviousMonth() {
        if (months.size > maxSize) {
            months.removeLast()
        }

        val first = months.first()
        val previousMonth = first.month.minusMonths(1)
        months.add(
            0, CalendarMonth(
                weeks = getMonthWeeks(
                    month = previousMonth,
                    startIndex = lookupStartIndex(previousMonth = previousMonth)
                ),
                month = previousMonth,
                id = nextId()
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

    private fun lookupStartIndex(previousMonth: ImmutableYearMonth): Int {
        val firstDay = previousMonth.atDay(1).dayOfWeek
        return daysOfWeek.indexOfFirst { it == firstDay }
    }

    private fun getMonthWeeks(
        month: ImmutableYearMonth,
        startIndex: Int,
    ): List<CalendarWeek> {
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
                        date = ImmutableLocalDate(
                            month.atDay(
                                lastAddedDayIndex
                            )
                        ),
                        isFirstWeekDay = i == 0,
                        isLastWeekDay = i == daysOfWeekSizeMinusStartIndex - 1
                    )
                },
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
                    },
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
                }, firstDayIndex = 0, lastDayIndex = lastWeekSize - 1, totalSize = daysOfWeek.size
            )
        )

        return weeks
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun saver(): Saver<CalendarState, *> = Saver(save = {
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
    initialMonth: ImmutableYearMonth = ImmutableYearMonth.now(),
    daysOfWeek: List<DayOfWeek> = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )
): CalendarState {
    return rememberSaveable(saver = CalendarState.saver()) {
        CalendarState(
            initialMonth = initialMonth,
            daysOfWeek = daysOfWeek
        )
    }
}
