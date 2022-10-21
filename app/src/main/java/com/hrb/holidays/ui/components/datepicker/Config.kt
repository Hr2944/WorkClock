package com.hrb.holidays.ui.components.datepicker

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hrb.holidays.commons.immutable.ImmutableDateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object DateFormatDefaults {
    val monthFormatter: ImmutableDateTimeFormatter =
        ImmutableDateTimeFormatter.ofPattern("MMMM yyyy")
    val dayFormatter: ImmutableDateTimeFormatter =
        ImmutableDateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val headerFormatter: ImmutableDateTimeFormatter =
        ImmutableDateTimeFormatter.ofPattern("MMM dd", Locale.getDefault())
}

object CalendarTypography {
    val month: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold)
    val day: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center)
    val week: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography.body2
}
