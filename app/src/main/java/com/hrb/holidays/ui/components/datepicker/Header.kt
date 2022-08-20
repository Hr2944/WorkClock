package com.hrb.holidays.ui.components.datepicker

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.hrb.holidays.commons.ImmutableDateTimeFormatter
import com.hrb.holidays.commons.capitalizeFirstChar
import java.time.format.DateTimeFormatter

@Composable
internal fun Dates(
    separator: String,
    style: TextStyle,
    pickerState: DateRangePickerState
) {
    val dateFormatter = remember {
        ImmutableDateTimeFormatter(DateTimeFormatter.ofPattern("MMM dd"))
    }

    StartDateText(
        pickerState = pickerState,
        dateFormatter = dateFormatter,
        style = style,
        separator = separator
    )
    EndDateText(
        pickerState = pickerState,
        dateFormatter = dateFormatter,
        style = style,
    )
}

@Composable
fun StartDateText(
    pickerState: DateRangePickerState,
    dateFormatter: ImmutableDateTimeFormatter,
    style: TextStyle,
    separator: String
) {
    val alpha =
        if (pickerState.startDate == null) ContentAlpha.disabled
        else LocalContentAlpha.current

    Row {
        Text(
            text = pickerState.startDate?.format(dateFormatter())?.capitalizeFirstChar()
                ?: "Start",
            style = style,
            color = LocalContentColor.current.copy(alpha = alpha),
        )
        Text(
            text = separator,
            style = style,
            color = LocalContentColor.current.copy(alpha = alpha),
        )
    }
}

@Composable
fun EndDateText(
    pickerState: DateRangePickerState,
    dateFormatter: ImmutableDateTimeFormatter,
    style: TextStyle
) {
    val alpha =
        if (pickerState.endDate == null) ContentAlpha.disabled
        else LocalContentAlpha.current

    Text(
        text = pickerState.endDate?.format(dateFormatter())?.capitalizeFirstChar()
            ?: "End",
        style = style,
        color = LocalContentColor.current.copy(alpha = alpha),
    )

}

@Composable
internal fun ToggleButton(onClick: () -> Unit, icon: ImageVector) {
    IconButton(onClick = onClick) {
        Icon(icon, "Toggle between date range input and date range picker")
    }
}
