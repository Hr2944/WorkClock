package com.hrb.holidays.ui.components.datepicker.minimized

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hrb.holidays.commons.immutable.ImmutableLocalDate
import com.hrb.holidays.ui.components.datepicker.HeaderDates
import com.hrb.holidays.ui.components.datepicker.RangePickerState
import com.hrb.holidays.ui.components.datepicker.TogglePickerModeButton
import com.hrb.holidays.ui.components.textfields.DateTextField


@Composable
internal fun RangeTextInput(
    pickerState: RangePickerState,
    onToggleFullScreen: () -> Unit,
    onSave: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.width(328.dp)
    ) {
        Column {
            Header(
                pickerState = pickerState,
                onToggleFullScreen = onToggleFullScreen
            )
            Body(
                pickerState = pickerState,
                onSave = onSave,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
private fun Header(
    pickerState: RangePickerState,
    onToggleFullScreen: () -> Unit,
    background: Color = MaterialTheme.colors.primarySurface
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 24.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColorFor(background)) {
            Text(
                "ENTER RANGE",
                style = MaterialTheme.typography.overline,
                modifier = Modifier.paddingFromBaseline(top = 28.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 72.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    HeaderDates(
                        separator = " – ",
                        style = MaterialTheme.typography.h5,
                        pickerState = pickerState
                    )
                }
                TogglePickerModeButton(
                    onClick = onToggleFullScreen,
                    icon = Icons.Filled.CalendarToday
                )
            }
        }
    }
}

@Composable
private fun Body(
    pickerState: RangePickerState,
    onSave: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, top = 16.dp, end = 24.dp)
    ) {
        StartDateInput(pickerState = pickerState)
        EndDateInput(pickerState = pickerState)
        RangeInputActions(
            onSave = onSave,
            onDismissRequest = onDismissRequest,
            pickerState = pickerState
        )
    }
}

@Composable
fun RangeInputActions(
    onSave: () -> Unit,
    onDismissRequest: () -> Unit,
    pickerState: RangePickerState,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onDismissRequest, modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "CANCEL")
        }
        TextButton(onClick = onSave, enabled = pickerState.isValidRange) {
            Text(text = "OK")
        }
    }
}

@Composable
private fun StartDateInput(
    pickerState: RangePickerState
) {
    var isError by remember {
        mutableStateOf(false)
    }
    DateTextField(
        initialValue = pickerState.startDate,
        onValueChange = {
            isError = false
            if (it != null) {
                pickerState.selectDate(it)
            }
        },
        label = {
            Text(text = "Start Date")
        },
        isError = isError,
        onError = { isError = true },
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun EndDateInput(
    pickerState: RangePickerState
) {
    var isError by remember {
        mutableStateOf(false)
    }
    DateTextField(
        initialValue = pickerState.endDate,
        onValueChange = {
            if (it != null) {
                pickerState.selectDate(it)
            }
            isError = !pickerState.isValidRange
        },
        label = {
            Text(text = "End Date")
        },
        isError = isError,
        onError = { isError = true }
    )
}
