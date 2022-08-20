package com.hrb.holidays.ui.components.datepicker

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek
import java.time.LocalDate

@Stable
class DateRangePickerState(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    private val today: LocalDate = LocalDate.now()
) {
    var startDate: LocalDate? by mutableStateOf(startDate)
        private set

    var endDate: LocalDate? by mutableStateOf(endDate)
        private set

    val isValidRange: Boolean
        get() {
            val start = startDate
            val end = endDate
            return (start != null && end != null) && start.isBefore(end)
        }

    fun isToday(day: LocalDate): Boolean {
        return day == today
    }

    fun selectDate(date: LocalDate) {
        if (startDate == null) {
            startDate = date
        } else if (endDate == null && startDate != date && date.isAfter(startDate)) {
            endDate = date
        } else {
            startDate = date
            endDate = null
        }
    }

    fun isSelected(day: LocalDate): Boolean {
        return day == startDate || day == endDate
    }

    fun isInExclusiveRange(day: LocalDate): Boolean {
        return startDate?.isBefore(day) == true && endDate?.isAfter(day) == true
    }

    fun isStartDate(date: LocalDate): Boolean {
        return date == startDate
    }

    fun isEndDate(date: LocalDate): Boolean {
        return date == endDate
    }

    companion object {
        fun Saver(): Saver<DateRangePickerState, *> =
            Saver(
                save = { mapOf("startDate" to it.startDate, "endDate" to it.endDate) },
                restore = {
                    DateRangePickerState(
                        startDate = it["startDate"],
                        endDate = it["endDate"]
                    )
                }
            )
    }
}

@Composable
fun rememberDateRangePickerState(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    today: LocalDate = LocalDate.now()
): DateRangePickerState {
    return rememberSaveable(saver = DateRangePickerState.Saver()) {
        DateRangePickerState(
            startDate = startDate,
            endDate = endDate,
            today = today
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun DateRangePicker(
    onDismissRequest: () -> Unit,
    onSave: (startDate: LocalDate, endDate: LocalDate) -> Unit,
    pickerState: DateRangePickerState = rememberDateRangePickerState(),
    launchFullScreen: Boolean = false,
    daysOfWeek: ImmutableList<DayOfWeek> = persistentListOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
    )
) {
    var isFullScreen by rememberSaveable(launchFullScreen) {
        mutableStateOf(launchFullScreen)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        if (isFullScreen) {
            FullScreenDateRangePicker(
                onDismissRequest = onDismissRequest,
                onSave = {
                    val startDate = pickerState.startDate
                    val endDate = pickerState.endDate
                    if (startDate != null && endDate != null) {
                        onSave(
                            startDate,
                            endDate
                        )
                    }
                },
                onToggleFullScreen = { isFullScreen = !isFullScreen },
                onSelectDate = { date -> pickerState.selectDate(date) },
                daysOfWeek = daysOfWeek,
                pickerState = pickerState
            )
        } else {
            DateRangeInput(
                pickerState = pickerState,
                onToggleFullScreen = { isFullScreen = !isFullScreen },
                onSave = {
                    val startDate = pickerState.startDate
                    val endDate = pickerState.endDate
                    if (startDate != null && endDate != null) {
                        onSave(
                            startDate,
                            endDate
                        )
                    }
                },
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun DateRangePickerPreview() {
    var showDialog by rememberSaveable {
        mutableStateOf(true)
    }
    Button(onClick = {
        showDialog = true
    }) {
        Text("Show Dialog")
    }
    if (showDialog) {
        DateRangePicker(
            onDismissRequest = {
                showDialog = false
            },
            onSave = { _, _ ->
                showDialog = false
            }
        )
    }
}
