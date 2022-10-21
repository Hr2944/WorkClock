package com.hrb.holidays.ui.components.datepicker.fullscreen

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
import com.hrb.holidays.ui.components.datepicker.HeaderDates
import com.hrb.holidays.ui.components.datepicker.RangePickerState
import com.hrb.holidays.ui.components.datepicker.TogglePickerModeButton

@Composable
internal fun LandscapeHeader(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    fillWidthFraction: Float = .35f,
    pickerState: RangePickerState
) {
    BasicHeader(
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
            TogglePickerModeButton(onClick = onToggleFullScreen, icon = Icons.Filled.Edit)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
        ) {
            HeaderDates(
                separator = ",",
                style = MaterialTheme.typography.h4,
                pickerState = pickerState,
            )
        }
        HeaderActions(
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
internal fun PortraitHeader(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    pickerState: RangePickerState
) {
    BasicHeader(
        backgroundColor = backgroundColor,
    ) {
        HeaderActions(
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
                    HeaderDates(
                        separator = " – ",
                        style = MaterialTheme.typography.h5,
                        pickerState = pickerState,
                    )
                }
                TogglePickerModeButton(onClick = onToggleFullScreen, icon = Icons.Filled.Edit)
            }
        }
    }
}

@Composable
private fun BasicHeader(
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
private fun HeaderActions(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    pickerState: RangePickerState
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
            onSave = onSave,
            isValidDate = { pickerState.isValidRange }
        )
    }
}

@Composable
private fun SaveButton(
    onSave: () -> Unit,
    isValidDate: () -> Boolean
) {
    IconButton(onClick = onSave, enabled = isValidDate()) {
        Text(text = "SAVE", style = MaterialTheme.typography.button)
    }
}
