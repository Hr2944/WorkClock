package com.hrb.holidays.ui.components.datepicker

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.hrb.holidays.commons.capitalizeFirstChar
import com.hrb.holidays.commons.immutable.ImmutableLocalDate

@Composable
internal fun HeaderDates(
    separator: String,
    style: TextStyle,
    pickerState: RangePickerState
) {
    DateText(
        style = style,
        dateProvider = { pickerState.startDate },
        placeholder = "Start",
        separator = separator
    )
    DateText(
        style = style,
        dateProvider = { pickerState.endDate },
        placeholder = "End"
    )
}

@Composable
private fun DateText(
    style: TextStyle,
    dateProvider: () -> ImmutableLocalDate?,
    placeholder: String,
    separator: String = ""
) {
    val alpha =
        if (dateProvider() == null) ContentAlpha.disabled
        else LocalContentAlpha.current
    val text = remember(dateProvider(), placeholder, separator) {
        dateProvider()?.format(DateFormatDefaults.headerFormatter.toDateTimeFormatter())
            ?.capitalizeFirstChar()
            ?: placeholder
    }

    Row {
        Text(
            text = text,
            style = style,
            color = LocalContentColor.current.copy(alpha = alpha)
        )
        if (separator != "") {
            Text(
                text = separator,
                style = style,
                color = LocalContentColor.current.copy(alpha = alpha)
            )
        }
    }
}

@Composable
internal fun TogglePickerModeButton(onClick: () -> Unit, icon: ImageVector) {
    IconButton(onClick = onClick) {
        Icon(icon, "Toggle between date range input and date range picker")
    }
}
