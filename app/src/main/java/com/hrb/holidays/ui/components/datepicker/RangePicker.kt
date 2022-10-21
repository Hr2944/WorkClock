package com.hrb.holidays.ui.components.datepicker

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hrb.holidays.commons.immutable.ImmutableLocalDate
import com.hrb.holidays.ui.components.datepicker.fullscreen.AdaptiveRangePicker
import com.hrb.holidays.ui.components.datepicker.minimized.RangeTextInput
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek

@ExperimentalComposeUiApi
@Composable
fun RangePickerDialog(
    onDismissRequest: () -> Unit,
    onSave: (startDate: ImmutableLocalDate, endDate: ImmutableLocalDate) -> Unit,
    pickerState: RangePickerState = rememberRangePickerState(),
    launchFullScreen: Boolean = true,
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
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        RangePicker(
            onDismissRequest = onDismissRequest,
            onSave = onSave,
            pickerState = pickerState,
            launchFullScreen = launchFullScreen,
            daysOfWeek = daysOfWeek
        )
    }
}

@Composable
fun RangePicker(
    onDismissRequest: () -> Unit,
    onSave: (startDate: ImmutableLocalDate, endDate: ImmutableLocalDate) -> Unit,
    pickerState: RangePickerState = rememberRangePickerState(),
    launchFullScreen: Boolean = true,
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

    if (isFullScreen) {
        AdaptiveRangePicker(
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
        RangeTextInput(
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

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun RangePickerPreview() {
    var showDialog by rememberSaveable {
        mutableStateOf(true)
    }
    Button(onClick = {
        showDialog = true
    }) {
        Text("Show Dialog")
    }
    if (showDialog) {
        RangePickerDialog(
            onDismissRequest = {
                showDialog = false
            },
            onSave = { _, _ ->
                showDialog = false
            }
        )
    }
}
