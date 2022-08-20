package com.hrb.holidays.ui.components.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun FullScreenLandscapeHeader(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    fillWidthFraction: Float = .35f,
    pickerState: DateRangePickerState
) {
    Header(
        backgroundColor = backgroundColor,
        modifier = Modifier
            .fillMaxWidth(fillWidthFraction)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SELECT RANGE", style = MaterialTheme.typography.overline)
            ToggleButton(onClick = onToggleFullScreen, icon = Icons.Filled.Edit)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
        ) {
            Dates(
                separator = ",",
                style = MaterialTheme.typography.h4,
                pickerState = pickerState,
            )
        }
        DialogActions(
            onDismissRequest = onDismissRequest,
            onSave = onSave,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            pickerState = pickerState,
        )
    }
}

@Composable
internal fun FullScreenPortraitHeader(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    pickerState: DateRangePickerState
) {
    Header(
        backgroundColor = backgroundColor,
    ) {
        DialogActions(
            onDismissRequest = onDismissRequest,
            onSave = onSave,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            pickerState = pickerState,
        )
        Column(modifier = Modifier.padding(start = 60.dp)) {
            Text(
                "SELECT RANGE",
                style = MaterialTheme.typography.overline,
                modifier = Modifier.paddingFromBaseline(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Dates(
                        separator = " – ",
                        style = MaterialTheme.typography.h5,
                        pickerState = pickerState,
                    )
                }
                ToggleButton(onClick = onToggleFullScreen, icon = Icons.Filled.Edit)
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = backgroundColor)
            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 24.dp)
            .then(modifier),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColorFor(backgroundColor)) {
            content()
        }
    }
}

@Composable
private fun DialogActions(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    pickerState: DateRangePickerState
) {
    Row(
        modifier = modifier.padding(end = 4.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        IconButton(onClick = onDismissRequest) {
            Icon(Icons.Rounded.Close, "Close this dialog")
        }
        SaveButton(
            pickerState = pickerState,
            onSave = onSave
        )
    }
}

@Composable
private fun SaveButton(pickerState: DateRangePickerState, onSave: () -> Unit) {
    IconButton(onClick = onSave, enabled = pickerState.isValidRange) {
        Text(text = "SAVE", style = MaterialTheme.typography.button)
    }
}

