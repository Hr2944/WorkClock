package com.hrb.holidays.ui.components.datepicker

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.hrb.holidays.commons.immutable.ImmutableLocalDate

@Stable
class RangePickerState(
    startDate: ImmutableLocalDate? = null,
    endDate: ImmutableLocalDate? = null,
    today: ImmutableLocalDate = ImmutableLocalDate.now()
) {
    var startDate: ImmutableLocalDate? by mutableStateOf(startDate)
        private set

    var endDate: ImmutableLocalDate? by mutableStateOf(endDate)
        private set

    val today: ImmutableLocalDate by mutableStateOf(today)

    var isValidRange: Boolean by mutableStateOf(
        (startDate != null && endDate != null) && startDate.isBefore(endDate)
    )

    fun isToday(day: ImmutableLocalDate): Boolean {
        return day == today
    }

    fun selectDate(date: ImmutableLocalDate) {
        val currentEndDate = endDate
        val currentStartDate = startDate
        if (currentStartDate == null) {
            startDate = date
            isValidRange = false
        } else if (currentEndDate == null && date.isAfter(currentStartDate)) {
            endDate = date
            isValidRange = true
        } else {
            startDate = date
            endDate = null
            isValidRange = false
        }
    }

    fun isSelected(day: ImmutableLocalDate): Boolean {
        return day == startDate || day == endDate
    }

    fun isInExclusiveRange(day: ImmutableLocalDate): Boolean {
        return startDate?.isBefore(day) == true && endDate?.isAfter(day) == true
    }

    fun isStartDate(date: ImmutableLocalDate): Boolean {
        return date == startDate
    }

    fun isEndDate(date: ImmutableLocalDate): Boolean {
        return date == endDate
    }

    fun startAsString(): String {
        val start = this.startDate
        return start?.format(DateFormatDefaults.dayFormatter.toDateTimeFormatter()) ?: "Start Date"
    }

    fun endAsString(): String {
        val end = this.endDate
        return end?.format(DateFormatDefaults.dayFormatter.toDateTimeFormatter()) ?: "Start Date"
    }

    companion object {
        fun saver(): Saver<RangePickerState, *> =
            Saver(
                save = { mapOf("startDate" to it.startDate, "endDate" to it.endDate) },
                restore = {
                    RangePickerState(
                        startDate = it["startDate"],
                        endDate = it["endDate"]
                    )
                }
            )
    }
}

@Composable
fun rememberRangePickerState(
    startDate: ImmutableLocalDate? = null,
    endDate: ImmutableLocalDate? = null,
    today: ImmutableLocalDate = ImmutableLocalDate.now()
): RangePickerState {
    return rememberSaveable(saver = RangePickerState.saver()) {
        RangePickerState(
            startDate = startDate,
            endDate = endDate,
            today = today
        )
    }
}
