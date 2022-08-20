package com.hrb.holidays.ui.components.datepicker

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
import com.hrb.holidays.commons.ImmutableLocalDate
import com.hrb.holidays.ui.components.textfields.DateTextField


@Composable
internal fun DateRangeInput(
    pickerState: DateRangePickerState,
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
    pickerState: DateRangePickerState,
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
                    Dates(
                        separator = " – ",
                        style = MaterialTheme.typography.h5,
                        pickerState = pickerState
                    )
                }
                ToggleButton(onClick = onToggleFullScreen, icon = Icons.Filled.CalendarToday)
            }
        }
    }
}

@Composable
private fun Body(
    pickerState: DateRangePickerState,
    onSave: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, top = 16.dp, end = 24.dp)
    ) {
        var isStartError by remember {
            mutableStateOf(false)
        }
        DateTextField(
            initialValue = pickerState.startDate?.let { ImmutableLocalDate(it) },
            onValueChange = {
                isStartError = false
                if (it != null) {
                    pickerState.selectDate(it)
                }
            },
            label = {
                Text(text = "Start Date")
            },
            isError = isStartError,
            onError = { isStartError = true }
        )
        var isEndError by remember {
            mutableStateOf(false)
        }
        DateTextField(
            initialValue = pickerState.endDate?.let { ImmutableLocalDate(it) },
            onValueChange = {
                if (it != null) {
                    pickerState.selectDate(it)
                }
                isEndError = !pickerState.isValidRange
            },
            label = {
                Text(text = "End Date")
            },
            isError = isEndError,
            onError = { isEndError = true }
        )

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
}
